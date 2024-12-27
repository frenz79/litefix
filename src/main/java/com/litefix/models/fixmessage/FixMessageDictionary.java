package com.litefix.models.fixmessage;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.litefix.caches.NumbersCache;

public class FixMessageDictionary {

	public static final char DEFAULT_FILED_SEP = '';
	
	private final String beginString;
	private final char fieldSep;
	private final char decimalSep;
	
	private final Map<String,FixMessageFieldTemplate> header = new LinkedHashMap<>();
	private final Map<String,FixMessageFieldTemplate> trailer = new LinkedHashMap<>();
	private final Map<String,FixMessageTemplate> msgTypes = new HashMap<>(64);
	
	public FixMessageDictionary(String beginString,char decimalSep) {
		this(beginString, DEFAULT_FILED_SEP,decimalSep);
	}
	
	public FixMessageDictionary(String beginString, char fieldSep, char decimalSep) {
		super();
		this.beginString = beginString;
		this.fieldSep = fieldSep;
		this.decimalSep = decimalSep;
	}	
	
	public FixMessageTemplate getTemplate( String msgType ) {
		return msgTypes.get(msgType);
	}
	
	public Collection<FixMessageFieldTemplate> getAllHeaderFields( ) {
		return header.values();
	}
	
	public FixMessageFieldTemplate getHeaderField( String id ) {
		return header.get(id);
	}
	
	public Collection<FixMessageFieldTemplate> getAllTrailerFields( ) {
		return trailer.values();
	}
	
	public FixMessageFieldTemplate getTrailerField( String id ) {
		return header.get(id);
	}
	
	public static enum FieldType {
		STRING,
		CHAR,
		BOOLEAN,
		INTEGER,
		DECIMAL,
		UTC_TIMESTAMP,
		GROUP_SIZE
	}
	
	public FixMessageDictionary addTrailer( int id, String name, boolean mandatory, FieldType type) {
		return addTrailer(id, name, mandatory, type, null);
	}

	public FixMessageDictionary addTrailer( int id, String name, boolean mandatory, FieldType type, Object defaultVal ) {
		this.trailer.put(NumbersCache.toString(id), new FixMessageFieldTemplate(id, name, mandatory, type).setValue(defaultVal));
		return this;
	}
	
	public FixMessageDictionary addHeader( int id, String name, boolean mandatory, FieldType type) {
		return addHeader(id, name, mandatory, type, null);
	}

	public FixMessageDictionary addHeader( int id, String name, boolean mandatory, FieldType type, Object defaultVal ) {
		this.header.put(NumbersCache.toString(id), new FixMessageFieldTemplate(id, name, mandatory, type).setValue(defaultVal));
		return this;
	}
		
	public static class FixMessageFieldTemplate {
		private final int id;
		private final String name;
		private final boolean mandatory;
		private final FieldType type;
		private Object value;
		
		public FixMessageFieldTemplate(int id, boolean mandatory, FieldType type) {
			this(id, "", mandatory, type);
		}
		
		public FixMessageFieldTemplate(int id, String name, boolean mandatory, FieldType type) {
			super();
			this.id = id;
			this.name = name;
			this.mandatory = mandatory;
			this.type = type;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public boolean isMandatory() {
			return mandatory;
		}

		public FieldType getType() {
			return type;
		}

		public Object getValue() {
			return value;
		}

		public FixMessageFieldTemplate setValue(Object value) {
			this.value = value;
			return this;
		}
	}
	
	public class FixMessageTemplate {
		private final String msgType;
		
		private final Map<String,FixMessageFieldTemplate> fields = new LinkedHashMap<>();
		
		public FixMessageTemplate(String msgType) {
			super();
			this.msgType = msgType;
		}
		
		// Body
		public FixMessageTemplate addField( int id, String name, boolean mandatory, FieldType type) {
			return addField(id, name, mandatory, type, null);
		}

		public FixMessageTemplate addField( int id, String name, boolean mandatory, FieldType type, Object defaultVal ) {
			this.fields.put(NumbersCache.toString(id), new FixMessageFieldTemplate(id, name, mandatory, type).setValue(defaultVal));
			return this;
		}
		
		public FixMessageTemplate addGroup( int id, String name, boolean mandatory, FixMessageFieldTemplate[] groupFields  ) {
			this.fields.put(NumbersCache.toString(id), new FixMessageFieldTemplate(id, name, mandatory, FieldType.GROUP_SIZE).setValue(groupFields));
			return this;
		}
		
		public FixMessageTemplate addField( FixMessageFieldTemplate field ) {
			this.fields.put(NumbersCache.toString(field.getId()), field);
			return this;
		}

		public String getMsgType() {
			return msgType;
		}
		
		public FixMessageFieldTemplate get( int id) {
			return get(NumbersCache.toString(id));
		}
		
		public FixMessageFieldTemplate get( String id) {
			return this.fields.get(id);
		}
		
		public Collection<FixMessageFieldTemplate> getAllFields() {
			return fields.values();
		}
	}
	
	public FixMessageTemplate register( String msgType ) {		
		if (msgTypes.containsKey(msgType)) {
			throw new RuntimeException(String.format("MsgType:%s already registered in dictionary",msgType ));
		}
		FixMessageTemplate entry = new FixMessageTemplate( msgType );
		msgTypes.put(msgType, entry);
		return entry;
	}

	public char getFieldSep() {
		return fieldSep;
	}

	public char getDecimalSep() {
		return decimalSep;
	}

	public String getBeginString() {
		return beginString;
	}	
}
