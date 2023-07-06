package com.litefix.modules.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.litefix.commons.utils.ByteUtils;
import com.litefix.modules.IPersistence;

public class FSPersistence<T extends com.litefix.modules.Cloneable> implements IPersistence<T>{

	private final String outgoingFile = "outgoing.dat";
	private final String incomingFile = "incoming.dat";
	private final FileOutputStream  outgoing;
	private final FileOutputStream  incoming;
	private InMemoryPersistence<T> memPersistence;
	
	public FSPersistence(String beginString, String senderCompId, String targetCompId) throws IOException {
		memPersistence = new InMemoryPersistence<>(beginString, senderCompId, targetCompId);
		load();
		outgoing = new FileOutputStream (new File(outgoingFile));
		incoming = new FileOutputStream (new File(incomingFile));
	}
	
	private void load() throws IOException {
		File f1 = new File(outgoingFile);
		if (!f1.exists()) return;
		
		File f2 = new File(incomingFile);
		if (!f2.exists()) return;
		
		FileInputStream outgoing = new FileInputStream (f1);
		FileInputStream incoming = new FileInputStream (f2);
		
		while (outgoing.available()>0) {
			byte[] seqBytes = outgoing.readNBytes(4);
			if ( seqBytes==null ||  seqBytes.length==0 ) break;
			
			int sequence = ByteUtils.bytesToInt( seqBytes );
			int buffSize = ByteUtils.bytesToInt( outgoing.readNBytes(4) );
			byte[] buff = outgoing.readNBytes(buffSize);
			
			try (ByteArrayInputStream bis = new ByteArrayInputStream(buff);
				 ObjectInputStream ois = new ObjectInputStream(bis)) {
				 T deserializedObj = (T) ois.readObject();
				 memPersistence.storeOutgoingMessage(sequence, deserializedObj);
			} catch (EOFException ex1) {
				break;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}			
		}
		
		while (incoming.available()>0) {
			byte[] seqBytes = incoming.readNBytes(4);
			if ( seqBytes==null ||  seqBytes.length==0 ) break;
			
			int sequence = ByteUtils.bytesToInt( seqBytes );
			memPersistence.setLastIncomingSeq(sequence);
		}
		
		outgoing.close();
		incoming.close();
	}
	 
	@Override
	public int getAndIncrementOutgoingSeq() {
		return memPersistence.getAndIncrementOutgoingSeq();
	}

	@Override
	public void storeOutgoingMessage(int sequence, T message) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			 ObjectOutputStream oos = new ObjectOutputStream(bos)) {
				outgoing.write(ByteUtils.intToBytes(sequence));
				outgoing.write(ByteUtils.intToBytes(bos.size()));
				outgoing.write(bos.toByteArray());
				outgoing.flush();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}		
		memPersistence.storeOutgoingMessage(sequence, message);
	}

	@Override
	public List<T> getAllOutgoingMessagesInRange(int valueAsInt, int valueAsInt2) {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public T findOutgoingMessageBySeq(int seq) {
		return (T)memPersistence.findOutgoingMessageBySeq(seq);
	}

	@Override
	public int getLastOutgoingSeq() {
		return memPersistence.getLastOutgoingSeq();
	}

	@Override
	public void reset() {
		memPersistence.reset();
	}

	@Override
	public int getLastIncomingSeq() {
		int seq = memPersistence.getLastIncomingSeq();
		return seq;
	}

	@Override
	public int setLastIncomingSeq( int sequence ) {
		try {
			incoming.write(ByteUtils.intToBytes(sequence));
			incoming.flush();
			return memPersistence.setLastIncomingSeq(sequence);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}		
	}

	@Override
	public void close() {
		try {
			incoming.flush();
			outgoing.flush();
			incoming.close();
			outgoing.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
