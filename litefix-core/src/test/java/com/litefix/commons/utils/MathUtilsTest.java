package com.litefix.commons.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

public class MathUtilsTest {

	@Test
	public void calcChecksumTest( ) {		
		byte[] buff = "dsjfh273wufnsdjks8439tèdijovkdj09t8490t3".getBytes();
		MathUtils.calcChecksum(buff, 0, buff.length);
		
		assertEquals(239, MathUtils.calcChecksum(buff, 0, buff.length));
		
		buff = (  "7we64fnsmn2897nsjk3897rdhcj23897rhn3407rufhdsn09fjhfh24rs"
				+ "'4ruidì7ruffjhsdy230r7fu89sdh298r82iohcsdohf2305723rjfpàd"
				+ "ffu43'èr0ufjsdoihv9èu0fjdsioàhy4r0ufwioshv9èvhwiofy20r9uf"
				+ "jàu2'5874'2è0ufjsoidfy349è8fhsduifwoèàrofjwvcdirioruierew").getBytes();
		
		assertEquals(44, MathUtils.calcChecksum(buff, 0, buff.length));
	}
	
	@Test
	public void calcChecksumTestPerf( ) {
		int testsCount = 10000;
		List<byte[]> testBuffs = new ArrayList<>();
		for ( int i=0; i<testsCount; i++ ) {
			testBuffs.add(
				(UUID.randomUUID().toString() + UUID.randomUUID().toString()).getBytes() );
		}
		
		// Warmup
		for ( int j=0; j<1000; j++ ) {
			for ( int i=0; i<testsCount; i++ ) {
				byte[] buff = testBuffs.get(i);
				MathUtils.calcChecksum(buff, 0, buff.length);
			}
		}
		
		// best time:157_594
		long startTime = System.nanoTime();
		for ( int j=0; j<1000; j++ ) {
			for ( int i=0; i<testsCount; i++ ) {
				byte[] buff = testBuffs.get(i);
				MathUtils.calcChecksum(buff, 0, buff.length);
			}
		}
		long endTime = System.nanoTime();
		
		System.out.println("time:"+TimeUnit.NANOSECONDS.toMicros(endTime-startTime));
	}
}
