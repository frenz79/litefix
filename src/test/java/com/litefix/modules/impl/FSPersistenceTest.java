package com.litefix.modules.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class FSPersistenceTest {

	public static class DummyMessage implements com.litefix.modules.Cloneable {
		
		public final int a;
		public final int b;
		public final String string;
		public final byte[] buff;
		
		public DummyMessage(int a, int b, String string) {
			super();
			this.a = a;
			this.b = b;
			this.string = string;
			this.buff = string.getBytes();
		}
		
		@Override
		public DummyMessage clone() {		
			return new DummyMessage(a,b,string);
		}

		@Override
		public String toString() {
			return "DummyMessage [a=" + a + ", b=" + b + ", string=" + string + ", buff=" + Arrays.toString(buff) + "]";
		}
		
	}
	
	@Test
	public void test() throws IOException {
		FSPersistence<DummyMessage> persistence = 
			new FSPersistence<>("beginString", "senderCompId", "targetCompId");
		
		persistence.storeOutgoingMessage(1, new DummyMessage(1,2,"test1"));
		persistence.storeOutgoingMessage(2, new DummyMessage(2,3,"test2"));
		persistence.storeOutgoingMessage(3, new DummyMessage(3,4,"test3"));
		persistence.storeOutgoingMessage(4, new DummyMessage(4,5,"test4"));
		persistence.storeOutgoingMessage(5, new DummyMessage(5,6,"test5"));
		
		persistence.close();
		
		persistence = 
			new FSPersistence<>("beginString", "senderCompId", "targetCompId");
		
		assertEquals(5, persistence.getLastOutgoingSeq());
		assertEquals("test1", persistence.findOutgoingMessageBySeq(1).string );		
	}
}
