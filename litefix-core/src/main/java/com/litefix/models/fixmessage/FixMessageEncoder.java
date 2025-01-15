package com.litefix.models.fixmessage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.litefix.caches.NumbersCache;
import com.litefix.commons.utils.MathUtils;
import com.litefix.models.fixmessage.FixMessageDictionary.FieldType;
import com.litefix.models.fixmessage.FixMessageDictionary.FixMessageFieldTemplate;
import com.litefix.models.fixmessage.FixMessageDictionary.FixMessageTemplate;

public class FixMessageEncoder extends AbstractEncoderDecoder {

	private final FixMessageTemplate dictionaryMsgTemplate;
	private int seqNum;
	private final String msgType;
	
	private StringBuilder messageBodyStr = new StringBuilder();
	private Map<String,FixMessageField> msgFields = new HashMap<>();
	
	private FixMessageEncoder( FixMessageDictionary dictionary, String msgType ) {
		super(dictionary);
		this.dictionaryMsgTemplate = dictionary.getTemplate(msgType);
		this.msgType = msgType;
		if ( this.dictionaryMsgTemplate==null ) {
			throw new RuntimeException(String.format("Unknwon msgType:%s", msgType));
		}
	}
	
	public static FixMessageEncoder newEncoder( FixMessageDictionary dictionary, String msgType ) {
		FixMessageEncoder encoder  = new FixMessageEncoder( dictionary, msgType ); 
		encoder.set("35", msgType);
		return encoder;
	}
	
	private FixMessageFieldTemplate findFieldInTemplates( String fieldId ) {
		FixMessageFieldTemplate f = this.dictionaryMsgTemplate.get(fieldId);
		if ( f==null ) {
			f = this.dictionary.getHeaderField(fieldId);			
			if ( f==null ) {
				f = this.dictionary.getTrailerField(fieldId);
			}
		}
		
		if (f==null) {
			throw new RuntimeException(String.format("Unknown fieldId:%s",fieldId ));
		}
		return f;
	}
	
	public Object get( int id ) {
		return msgFields.get( NumbersCache.toString(id)).getValue();
	}
	
	
	public FixMessageEncoder set( int id, String val ) {
		return set( NumbersCache.toString(id), val);
	}
	
	public FixMessageEncoder set( int id, char val ) {
		return set( NumbersCache.toString(id), Character.valueOf(val));
	}
	
	public FixMessageEncoder set( int id, boolean val ) {
		return set( NumbersCache.toString(id), Boolean.valueOf(val));
	}
	
	public FixMessageEncoder set( int id, int val ) {
		if (id==34) {
			setSeqNum(val);
		}
		return set( NumbersCache.toString(id), Integer.valueOf(val));
	}
	
	public FixMessageEncoder set( int id, long val ) {
		return set( NumbersCache.toString(id), Long.valueOf(val));
	}
	
	public FixMessageEncoder set( String id, Object val ) {	
		FixMessageFieldTemplate template = findFieldInTemplates(id);
		this.msgFields.put(id, new FixMessageField(template, val));
		return this;
	}
	
	StringBuilder buildBodyStringBuilder( boolean force ) {
		if ( messageBodyStr.length()==0 || force ) {
			// Build Header
			appendFields( messageBodyStr, dictionary.getAllHeaderFields() );
			// Build Body
			appendFields( messageBodyStr, dictionaryMsgTemplate.getAllFields() );		
			// Fill Trailer
			appendFields( messageBodyStr, dictionary.getAllTrailerFields() );
		}
		return messageBodyStr;
	}
	
	public byte[] build() {
		return build( false );
	}
	
	public byte[] forceBuild() {
		return build( true );
	}
	
	private byte[] build( boolean force ) {
		buildBodyStringBuilder( force );
		setPrefix( messageBodyStr );
		setChecksum( messageBodyStr );
		return messageBodyStr.toString().getBytes();
	}
	
	private void setPrefix( final StringBuilder strBld ) {
		int bodyLen = strBld.length();	
		StringBuilder pre = new StringBuilder()
			.append("8=")
			.append(dictionary.getBeginString())
			.append(dictionary.getFieldSep())
			.append("9=")
			.append(NumbersCache.toString(bodyLen))
			.append(dictionary.getFieldSep());
		strBld.insert(0, pre);
	}
	
	private void setChecksum( final StringBuilder strBld ) {
		int checksum = MathUtils.calcChecksum(strBld);		
		strBld.append("10")
			.append('=')
			.append(NumbersCache.toPaddedString(checksum))
			.append(dictionary.getFieldSep());
	}
	
	public String buildString() {
		return new String(build());
	}
	
	private void appendFields( final StringBuilder strBld, final Collection<FixMessageFieldTemplate>  templateFields) {
		for ( FixMessageFieldTemplate fieldTemplate : templateFields ) {
			String fieldId = NumbersCache.toString(fieldTemplate.getId());			
			FixMessageField fieldValue = msgFields.get( fieldId );
			
			if ( fieldValue!=null && !FieldType.RESERVED.equals( fieldTemplate.getType())) {
				strBld.append(fieldId).append('=');
				
				if ( fieldValue.getValue()!=null ) {
					strBld.append( String.valueOf(fieldValue.getValue()) );
				} else {
					if ( fieldTemplate.isMandatory() && fieldTemplate.getValue()==null ) {
						throw new RuntimeException(
							String.format("Mandatory field:%s(%d) not set and no default value configured", 
								fieldTemplate.getName(), fieldTemplate.getId()));
					}
					strBld.append( String.valueOf(fieldTemplate.getValue()) );
				} 
				strBld.append(dictionary.getFieldSep());
			}
		}
	}
	
	static class FixMessageField extends FixMessageFieldTemplate {
	
		public FixMessageField( FixMessageFieldTemplate template, Object val) {
			super(template.getId(), template.getName(), template.isMandatory(), template.getType() );
			setValue(val);
		}
	}

	public int getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public String getMsgType() {
		return msgType;
	}

	@Override
	public String toString() {
		return "FixMessageEncoder [seqNum=" + seqNum + ", msgType=" + msgType + ", messageBodyStr=" + messageBodyStr
				+ "]";
	}
	
}
