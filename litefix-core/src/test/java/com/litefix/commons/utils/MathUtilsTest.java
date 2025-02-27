package com.litefix.commons.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

public class MathUtilsTest {

	@Test
	void scalingTest() {
		assertEquals(968_899_900_000_000l, MathUtils.toUnscaledNumber("96889.99000000"));
		assertEquals(56_200_000l, MathUtils.toUnscaledNumber("0.00562000"));
		assertEquals(969_989_900_000_000l, MathUtils.toUnscaledNumber("96998.99000000"));
		assertEquals(37_200_000l, MathUtils.toUnscaledNumber("0.00372000"));
		
		assertEquals(968_890_000_000_000l, MathUtils.toUnscaledNumber("96889.00000000"));
		
		assertEquals("96889.9900000000", MathUtils.toScaledString(968_899_900_000_000l));
		assertEquals("0.0056200000", MathUtils.toScaledString(56_200_000l));
		assertEquals("96998.9900000000", MathUtils.toScaledString(969_989_900_000_000l));
		assertEquals("0.0037200000", MathUtils.toScaledString(37_200_000l));	
		
		assertEquals("96889.0000000000", MathUtils.toScaledString(968_890_000_000_000l));
	}
	
	@Test
	public void calcFixChecksumTest( ) {		
		byte[] buff = "dsjfh273wufnsdjks8439tèdijovkdj09t8490t3".getBytes();
		MathUtils.calcFixChecksum(buff, 0, buff.length);
		
		assertEquals(239, MathUtils.calcFixChecksum(buff, 0, buff.length));
		
		buff = (  "7we64fnsmn2897nsjk3897rdhcj23897rhn3407rufhdsn09fjhfh24rs"
				+ "'4ruidì7ruffjhsdy230r7fu89sdh298r82iohcsdohf2305723rjfpàd"
				+ "ffu43'èr0ufjsdoihv9èu0fjdsioàhy4r0ufwioshv9èvhwiofy20r9uf"
				+ "jàu2'5874'2è0ufjsoidfy349è8fhsduifwoèàrofjwvcdirioruierew").getBytes();
		
		assertEquals(44, MathUtils.calcFixChecksum(buff, 0, buff.length));
	}
	
	@Test
	public void calcFixChecksumTestPerf( ) {
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
				MathUtils.calcFixChecksum(buff, 0, buff.length);
			}
		}
		
		// best time:157_594
		long startTime = System.nanoTime();
		for ( int j=0; j<1000; j++ ) {
			for ( int i=0; i<testsCount; i++ ) {
				byte[] buff = testBuffs.get(i);
				MathUtils.calcFixChecksum(buff, 0, buff.length);
			}
		}
		long endTime = System.nanoTime();
		
		System.out.println("time:"+TimeUnit.NANOSECONDS.toMicros(endTime-startTime));
	}
}
