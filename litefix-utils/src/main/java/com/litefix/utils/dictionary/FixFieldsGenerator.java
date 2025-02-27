package com.litefix.utils.dictionary;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FixFieldsGenerator {
	
	public static void main ( String[] args ) throws Exception {
		File file = new File("D:\\SourceCode\\Incubator\\litefix\\litefix-utils\\src\\main\\resources\\dictionaries\\FIX44.xml");
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document doc = documentBuilder.parse(file);

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		
	    NodeList fieldsList = (NodeList) xpath.compile("/fix/fields//field").evaluate(doc, XPathConstants.NODESET);

	    generateClassPrefix(
	    	"DefaultFix44Fields"
	    );
	    
		for ( int i=0; i<fieldsList.getLength(); i++ ) {
			Node item = fieldsList.item(i);
			String number = item.getAttributes().getNamedItem("number").getTextContent();
			String name = item.getAttributes().getNamedItem("name").getTextContent();
			String type = item.getAttributes().getNamedItem("type").getTextContent();
			
			generateField( number, name, type );
		}
		generateLookupMaps();
		generateClassPostfix();
	}
	
	private static final Map<Integer,String> fixFieldByTag = new HashMap<>();
	
	
	public static void generateClassPrefix( String className ) {
		System.out.println("package com.litefix.models.dictionary;");
		System.out.println("");
		System.out.println("import com.litefix.models.fixmessage.FixMessageDictionary.FieldType;");
		System.out.println("import java.util.HashMap;");
		System.out.println("import java.util.Map;");
		System.out.println("");
		System.out.println("// Generated at:"+LocalDateTime.now().atZone(ZoneId.of("UTC"))+"");
		System.out.println("");
		System.out.println("public class "+className+" {");
		System.out.println("");
		System.out.println("public FixField of(int tag) { return fieldsByTag[tag]; }");
		System.out.println("public FixField of(String name) { return fieldsByName.get(name); }");
		
		System.out.println("");
		
		System.out.println("public void add( FixField f ) {");
		System.out.println("fieldsByTag[f.tag()] = f;");
		System.out.println("fieldsByName.put(f.name(), f);");
		System.out.println("}");
		
		System.out.println("");
		
		System.out.println("");
	}
	
	public static void generateClassPostfix() {
		System.out.println("");
		System.out.println("}");
	}
	
	public static void generateField(String number , String name ,String type ) {
		System.out.println(
			"public static final FixField "+name+"                   = new FixField(\""+name+"\"                ,   "+number+", FieldType."+getFieldType(type)+"       );"
		);
		
		fixFieldByTag.put(Integer.valueOf(number), name);
	}
	
	public static void generateLookupMaps() {
		System.out.println("");
		System.out.println("private static final FixField[] fieldsByTag = new FixField[]{");
		
		StringBuilder strTag = new StringBuilder();
		StringBuilder strName = new StringBuilder();
		for (int i=1; i<fixFieldByTag.size(); i++) {
			strTag.append(fixFieldByTag.get(i)+",\n");			
			strName.append("fieldsByName.put(\""+fixFieldByTag.get(i)+"\","+fixFieldByTag.get(i)+");\n");
		}
		strTag.setLength(strTag.length()-2);
		strName.setLength(strName.length()-1);
		
		System.out.println(strTag);
		System.out.println("};");
		
		System.out.println("");
		System.out.println("private static final Map<String,FixField> fieldsByName = new HashMap<>();");
		System.out.println("static {");
		System.out.println(strName);
		System.out.println("}");
	}
	
	public static String getFieldType(String type ) {
		switch(type) {
			case "STRING" :  
			case "CURRENCY" :  
			case "MULTIPLEVALUESTRING" :  
			case "EXCHANGE" :
			case "DATA" :
			case "MONTHYEAR" :
			case "COUNTRY" :
				return "STRING";
			
			case "SEQNUM" :  
			case "LENGTH" :  
			case "INT" :
				return "INTEGER";
			
			case "AMT" : 
			case "QTY" : 
			case "PRICE" : 
			case "FLOAT" :
			case "PERCENTAGE" :
			case "PRICEOFFSET" :
				return "DECIMAL";
			
			case "CHAR" :  
				return "CHAR";
				
			case "UTCTIMESTAMP" :
			case "LOCALMKTDATE" :
			case "UTCDATEONLY" :
			case "UTCTIMEONLY" :
				return "UTC_TIMESTAMP";
			
			case "BOOLEAN" :
				return "BOOLEAN";
				
			case "NUMINGROUP" :
				return "GROUP_SIZE";
		}
		
		return "@"+type;
	}
}
