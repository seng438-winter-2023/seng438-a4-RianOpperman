package org.jfree.data;

import static org.junit.Assert.*;
import org.jfree.data.Range;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.security.InvalidParameterException;

public class RangeTest{
	private Range exampleRange;
	
	// Used to detect when an exception has been thrown
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	// Sets up a Range object for use later in every test
	@Before
	public void setUp() throws Exception {
		exampleRange = new Range(-5, 5);
	}
	
	// -----------------------------------------------------------
	// getLowerBound()
	
	// Tests getting the lower bound from a Range object
	@Test
	public void getLowerBoundTest() {
		assertEquals("Retrieved lower bound incorrect",
				-5, exampleRange.getLowerBound(), .000000001d);
	}

	// -----------------------------------------------------------
	// getUpperBound()
	
	// Tests getting the upper bound from a Range object
	@Test
	public void getUpperBoundTest() {
		assertEquals("Retrieved upper bound incorrect",
				5, exampleRange.getUpperBound(), .000000001d);
	}

	// -----------------------------------------------------------
	// getLength()
	
	// 
	@Test
	public void getLengthTest() {
		assertEquals("Retrieved length is incorrect", 
				10, exampleRange.getLength(),.000000001d);
	}

	// -----------------------------------------------------------
	// getCentralValue()
	
	// 
	@Test
	public void getCentralValueTest() {
		assertEquals("Retrieved central value is incorrect", 
				0, exampleRange.getCentralValue(),.000000001d);
	}

	// -----------------------------------------------------------
	// contains()
	
	// Contains() method is tested by sending a value which is below range
	@Test
	public void testContainsBelowRange() {
		double testValue = -6;
		assertFalse(exampleRange.contains(testValue));
	}

	// Contains() method is tested by sending a value which is above range
	@Test
	public void testContainsAboveRange() {
		double testValue = 6;
		assertFalse(exampleRange.contains(testValue));
	}
	
	// Contains() method is tested by sending a value which is within the range
	@Test
	public void testContainsInRange() {
		double testValue1 = 3;
		assertTrue(exampleRange.contains(testValue1));	
	}

	// -----------------------------------------------------------
	// intersects()

	// Tests intersects() when the given range is below the example range
	@Test
	public void testIntersectBelowSpecifiedRange() {
		assertFalse(exampleRange.intersects(6, 10));
	}

	
	@Test
	public void testIntersectAtLowervSpecifiedRange() {
		assertTrue(exampleRange.intersects(4, 10));
	}
	@Test
	public void testIntersectAboveSpecifiedRange() {
		assertFalse(exampleRange.intersects(-10, -6));
	}
	
	@Test
	public void testIntersectAtUpperSpecifiedRange() {
		assertTrue(exampleRange.intersects(-10, -4));
	}
	
	@Test
	public void testIntersectBelowGivenRange() {
		Range SpecifiedRange = new Range(6,10);
		assertFalse(exampleRange.intersects(SpecifiedRange));
	}
	
	@Test
	public void testIntersectAtLowervGivenRange() {
		Range SpecifiedRange = new Range(4,10);
		assertTrue(exampleRange.intersects(SpecifiedRange));
	}
	@Test
	public void testIntersectAboveGivenRange() {
		Range SpecifiedRange = new Range(-10,-6);
		assertFalse(exampleRange.intersects(SpecifiedRange));
	}
	
	@Test
	public void testIntersectAtUpperGivenRange() {
		Range SpecifiedRange = new Range(-10,-4);
		assertTrue(exampleRange.intersects(SpecifiedRange));
			
	}
	
	@Test 
	public void testIntersectWhenRangeIsNull() throws NullPointerException{
		exception.expect(NullPointerException.class);
		exampleRange.intersects(null);	
	}
	
	// -----------------------------------------------------------
	// constrain()
	@Test
	public void testConstraintBelowRange() {
		double testValue = -6;
		assertEquals("Return value should be -5",-5,
			exampleRange.constrain(testValue), 0.000000001d);		
	}
	
	//The Constraint() method is test when a value in the range 
	@Test
	public void testConstraintInRange() {
		double testValue = 3;
		assertEquals("Return value should be 3", 3,
			exampleRange.constrain(testValue), 0.000000001d);		
	}
	
	//The Constraint() method is test when a value above the range 
	@Test
	public void testConstraintAboveRange() {
		double testValue = 5;
		assertEquals("Return value should be 5", 5,
			exampleRange.constrain(testValue), 0.000000001d);	
	}

	// -----------------------------------------------------------
	// comnbine()

	// Tests combine when both arguments are null, expects a null result
	@Test
	public void testCombineBothNull(){
		Range ret = Range.combine(null, null);
		assertEquals(ret, null);
	}

	// Tests combine when first argument is null and second is valid,
	//   should return the second range
	@Test
	public void testCombineRange1Null(){
		Range ret = Range.combine(null, exampleRange);
		Range reference = new Range(-5, 5);
		assertEquals(reference, ret);
	}

	// Tests combine when first argument is valid and second is null,
	//   should return the first range
	@Test
	public void testCombineRange2Null(){
		Range ret = Range.combine(exampleRange, null);
		Range reference = new Range(-5, 5);
		assertEquals(ret, reference);
	}

	/* Need to test if:
	 * range1 == range2
	 * range2 in range1
	 * range2 out of range1
	 * range1 besides range2
	 */	

	// Tests combine when range 1 and range 2 are equal
	//   Should return a range that is similar to both range 1 and range 2
	@Test
	public void testCombineRange1EqualsRange2(){
		Range range1 = new Range(-10, 10);
		Range range2 = new Range(-10, 10);
		Range ret = Range.combine(range1, range2);
		assertEquals(ret, range1);
	}

	// Tests combine when range 2 is in range 1
	//   Should return a range 1 since it covers range 2
	@Test
	public void testCombineRange2InRange1(){
		Range range1 = new Range(-10, 10);
		Range range2 = new Range(-5, 5);
		Range ret = Range.combine(range1, range2);
		assertEquals(ret, range1);
	}

	// Tests combine when range 2 os outside of range 1
	// 	Should return a range that combines both, i.e. 
	// 	includes the lower bound of the lower range, 
	// 	and the upper bound of the higher range
	@Test
	public void testCombineRange2OutOfRange1(){
		Range range1 = new Range(-10, 10);
		Range range2 = new Range(-30, -20);
		Range ret = Range.combine(range1, range2);
		assertEquals(ret, new Range(-30, 10));
	}

	// Tests combine when range 1 is right beside range 2 
	// 	Should return a range that combines both of them,
	// 	meaning the new length is the addition of 
	// 	range1.length and range2.length, along with bounds
	// 	that work for this
	@Test
	public void testCombineRange1BesideRange2(){
		Range range1 = new Range(-10, 10);
		Range range2 = new Range(10, 30);
		Range ret = Range.combine(range1, range2);
		assertEquals(ret, new Range(-10, 30));
	}

	// -----------------------------------------------------------
	// combineIgnoringNaN()

	// tests combineIgnoringNaN when both ranges are null
	@Test
	public void testCombineIgnoringNaNBothNull(){
		Range ret = Range.combineIgnoringNaN(null, null);
		assertEquals(ret, null);
	}

	// tests combineIgnoringNaN when both ranges are NaN
	@Test
	public void testCombineIgnoringNaNBothNaN() {
		Range range1 = new Range(Double.NaN, Double.NaN);
		Range range2 = new Range(Double.NaN, Double.NaN);
		Range ret = Range.combineIgnoringNaN(range1, range2);
		assertEquals(ret, null);
	}

	// tests combineIgnoringNaN when range 1 is null and range 2 is valid
	@Test
	public void testCombineIgnoringNaNRange1Null(){
		Range ret = Range.combineIgnoringNaN(null, exampleRange);
		assertEquals(ret, exampleRange);
	}

	// tests combineIgnoringNaN when range1 is null and range 2 is NaN
	// 	Tests variations of range 2 being NaN, from both ends being 
	// 	NaN to only one end being NaN
	@Test
	public void testCombineIgnoringNaNRange1NullRange2NaN(){
		Range range2 = new Range(Double.NaN, Double.NaN);
		Range ret = Range.combineIgnoringNaN(null, range2);
		assertEquals(ret, null);

		range2 = new Range(Double.NaN, 5);
		ret = Range.combineIgnoringNaN(null, range2);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(ret.getUpperBound() == 5);

		range2 = new Range(-5, Double.NaN);
		ret = Range.combineIgnoringNaN(null, range2);
		assertTrue(Double.isNaN(ret.getUpperBound()));
		assertTrue(ret.getLowerBound() == -5);
	}

	// tests combineIgnoringNaN where range 2 is null and range 1 is valid
	@Test
	public void testCombineIgnoringNaNRange2Null(){
		Range ret = Range.combineIgnoringNaN(exampleRange, null);
		assertEquals(ret, exampleRange);
	}

	// tests combineIgnoringNaN where range 2 is null and range 1 is NaN
	// variations of range 1 are both ends are NaN, or one side is NaN
	@Test
	public void testCombineIgnoringNaNRange2NullRange1NaN(){
		Range range1 = new Range(Double.NaN, Double.NaN);
		Range ret = Range.combineIgnoringNaN(range1, null);
		assertEquals(ret, null);

		range1 = new Range(Double.NaN, 5);
		ret = Range.combineIgnoringNaN(range1, null);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(ret.getUpperBound() == 5);

		range1 = new Range(-5, Double.NaN);
		ret = Range.combineIgnoringNaN(range1, null);
		assertTrue(Double.isNaN(ret.getUpperBound()));
		assertTrue(ret.getLowerBound() == -5);
	}

	// Tests combine when range 1 and range 2 are equal
	//   Should return a range that is similar to both range 1 and range 2
	@Test
	public void testCombineIgnoringNaNRange1EqualsRange2(){
		Range range2 = new Range(-5, 5);
		Range ret = Range.combineIgnoringNaN(exampleRange, range2);
		assertEquals(ret, exampleRange);
	}

	// Tests combine when range 2 is in range 1
	//   Should return a range 1 since it covers range 2
	@Test
	public void testCombineIgnoringNaNRange2InRange1(){
		Range range2 = new Range(-3, 3);
		Range ret = Range.combineIgnoringNaN(exampleRange, range2);
		assertEquals(ret, exampleRange);
	}

	// Tests combine when range 2 os outside of range 1
	// 	Should return a range that combines both, i.e. 
	// 	includes the lower bound of the lower range, 
	// 	and the upper bound of the higher range
	@Test
	public void testCombineIgnoringNaNRange2OutOfRange1(){
		Range range2 = new Range(8, 10);
		Range ret = Range.combineIgnoringNaN(exampleRange, range2);
		assertEquals(ret, new Range(-5, 10));
	}

	// Tests combine when range 1 is right beside range 2 
	// 	Should return a range that combines both of them,
	// 	meaning the new length is the addition of 
	// 	range1.length and range2.length, along with bounds
	// 	that work for this
	@Test
	public void testCombineIgnoringNaNRange1BesideRange2(){
		Range range2 = new Range(5, 20);
		Range ret = Range.combineIgnoringNaN(exampleRange, range2);
		assertEquals(ret, new Range(-5, 20));
	}

	// -----------------------------------------------------------
	// expandToInclude()

	// tests expandToInclude where range 1 is null,
	// and value is either NaN or a regular double
	@Test
	public void testExpandToIncludeNullRange(){
		Range ret = Range.expandToInclude(null, Double.NaN);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(Double.isNaN(ret.getUpperBound()));

		ret = Range.expandToInclude(null, 5);
		assertEquals(ret, new Range(5, 5));
	}

	// tests expandToInclude when the value is low, i.e.
	// lower than the ranges lower bound
	@Test
	public void testExpandToIncludeValLow(){
		Range ret = Range.expandToInclude(exampleRange, -10);
		assertEquals(ret, new Range(-10, 5));
	}

	// tests expandToInclude when the value is NaN, should not expand as
	// NaN is weird
	@Test
	public void testExpandToIncludeValNaN(){
		Range ret = Range.expandToInclude(exampleRange, Double.NaN);
		assertEquals(ret, new Range(-5, 5));
	}

	// tests expandToInclude when the value is high, i.e.
	// higher than the ranges upper bound
	@Test
	public void testExpandToIncludeValHigh(){
		Range ret = Range.expandToInclude(exampleRange, 200);
		assertEquals(ret, new Range(-5, 200));
	}

	// tests expandToInclude when the value is inside the ranges bounds
	@Test
	public void testExpandToInclude(){
		Range ret = Range.expandToInclude(exampleRange, 2);
		assertEquals(ret, new Range(-5, 5));
	}

	// -----------------------------------------------------------
	// expand()

	// tests expand when range is null, should throw an IllegalArgumentException
	@Test
	public void testExpandRangeNull() throws IllegalArgumentException{
		exception.expect(IllegalArgumentException.class);
		Range ret = Range.expand(null, 0.5, 0.5);
	}

	// tests expand when range is NaN, should return same range as
	// NaN cannot be modified
	@Test
	public void testExpandRangeNaN(){
		Range ret = Range.expand(new Range(Double.NaN, Double.NaN), 0.5, 0.5);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(Double.isNaN(ret.getUpperBound()));
	}

	// tests expand when range is valid, both lower and upper margin are negative
	@Test
	public void testExpandNegLowerNegUpper(){
		Range ret = Range.expand(exampleRange, -0.25, -0.25);
		assertEquals(ret, new Range(-2.5, 2.5));
	}

	// tests expand when range is valid, lower margin is negative and upper margin is positive
	@Test
	public void testExpandNegLower(){
		Range ret = Range.expand(exampleRange, -0.25, 0.5);
		assertEquals(ret, new Range(-2.5, 10));
	}

	// tests expand when range is valid, lower margin is positive and upper margin is negative
	@Test
	public void testExpandNegUpper(){
		Range ret = Range.expand(exampleRange, 0.5, -0.25);
		assertEquals(ret, new Range(-10, 2.5));
	}

	// tests expand when range is valid and lower margin is NaN and upper margin is positive
	@Test
	public void testExpandNaNLower(){
		Range ret = Range.expand(exampleRange, Double.NaN, 0.5);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(ret.getUpperBound() == 10);
	}

	// tests expand when range is valid and lower margin is positive and upper margin is NaN
	@Test
	public void testExpandNaNUpper(){
		Range ret = Range.expand(exampleRange, 0.5, Double.NaN);
		assertTrue(Double.isNaN(ret.getUpperBound()));
		assertTrue(ret.getLowerBound() == -10);
	}

	// tests expand when range is valid and lower margin is smaller than the upper margin
	@Test
	public void testExpandLowerGreaterThanUpper(){
		Range ret = Range.expand(exampleRange, -0.25, -1);
		assertEquals(new Range(-3.75, -3.75), ret);
	}
	
	// tests expand when both upper and lower margin is positive and the range is valid
	@Test
	public void testExpand(){
		Range ret = Range.expand(exampleRange, 0.5, 0.5);
		assertEquals(new Range(-10, 10), ret);
	}

	// -----------------------------------------------------------
	// shift()

	// tests shift when range is null, should throw an IllegalArgumentException
	@Test
	public void testShiftRangeNull() throws IllegalArgumentException{
		exception.expect(IllegalArgumentException.class);
		Range ret = Range.shift(null, 5);
	}

	// tests shift when the Range is NaN, and has variations of NaN ranges,
	// including scenarios where both ends are NaN and where one end is NaN
	// Should return an unedited NaN bound as NaN cannot be modified
	@Test
	public void testShiftRangeNaN(){
		Range ret = Range.shift(new Range(Double.NaN, Double.NaN), 5);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(Double.isNaN(ret.getUpperBound()));

		ret = Range.shift(new Range(Double.NaN, 5), 5);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(ret.getUpperBound() == 10);

		ret = Range.shift(new Range(-5, Double.NaN), 5);
		assertTrue(Double.isNaN(ret.getUpperBound()));
		assertTrue(ret.getLowerBound() == 0);
	}

	// tests shift when range is valid and the delta is negative
	@Test
	public void testShiftDeltaNeg(){
		Range ret = Range.shift(exampleRange, -5);
		assertEquals(ret, new Range(-10, 0));
	}

	// tests shift when range is valid and the delta is positive
	@Test
	public void testShiftDeltaPos(){
		Range ret = Range.shift(exampleRange, 5);
		assertEquals(ret, new Range(0, 10));
	}

	// tests shift when range is valid and the delta is positive,
	// where a bound is 0 to cover one of the branches
	@Test
	public void testShift(){
		Range ret = Range.shift(new Range(0, 10), 10);
		assertEquals(ret, new Range(10, 20));
	}

	// tests shift with zero corssing set to true and range is null
	@Test
	public void testShiftZeroCrossingRangeNull() throws IllegalArgumentException{
		exception.expect(IllegalArgumentException.class);
		Range ret = Range.shift(null, -10, true);
	}

	// tests shift with zero crossing set to true and range is NaN
	// The ranges vary from both ends being NaN to only one end being NaN
	// The output of this test should not change the NaN bound as NaN 
	// 	cannot be modified
	@Test
	public void testShiftZeroCrossingRangeNaN(){
		Range ret = Range.shift(new Range(Double.NaN, Double.NaN), 5, true);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(Double.isNaN(ret.getUpperBound()));

		ret = Range.shift(new Range(Double.NaN, 5), 5, true);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(ret.getUpperBound() == 10);

		ret = Range.shift(new Range(-5, Double.NaN), 5, true);
		assertTrue(Double.isNaN(ret.getUpperBound()));
		assertTrue(ret.getLowerBound() == 0);
	}

	// tests shift with zero crossing set to true and delta being negative
	@Test
	public void testShiftZeroCrossingDeltaNeg(){
		Range ret = Range.shift(exampleRange, -10, true);
		assertEquals(ret, new Range(-15, -5));
	}

	// tests shift with zero crossing set to true and delta being positive
	@Test
	public void testShiftZeroCrossingDeltaPos(){
		Range ret = Range.shift(exampleRange, 10, true);
		assertEquals(ret, new Range(5, 15));
	}

	// -----------------------------------------------------------
	// scale()

	// tests scale when range is null, should throw IllegalArgumentException
	@Test
	public void testScaleRangeNull() throws IllegalArgumentException{
		exception.expect(IllegalArgumentException.class);
		Range ret = Range.scale(null, 2);
	}

	// tests scale when range is NaN, should not 
	// modify the NaN bound as NaN cannot be modified
	@Test
	public void testScaleRangeNaN(){
		Range ret = Range.scale(new Range(Double.NaN, Double.NaN), 5);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(Double.isNaN(ret.getUpperBound()));

		ret = Range.scale(new Range(Double.NaN, 5), 5);
		assertTrue(Double.isNaN(ret.getLowerBound()));
		assertTrue(ret.getUpperBound() == 25);
		
		ret = Range.scale(new Range(-5, Double.NaN), 5);
		assertTrue(Double.isNaN(ret.getUpperBound()));
		assertTrue(ret.getLowerBound() == -25);
	}

	// tests scale when the factor is less than 0,
	// should throw an IllegalArgumentException
	@Test
	public void testScaleFactorLessThan0() throws IllegalArgumentException{
		exception.expect(IllegalArgumentException.class);
		Range ret = Range.scale(exampleRange, -1);
	}

	// tests scale when range is valid and factor is valid
	@Test
	public void testScale(){
		Range ret = Range.scale(exampleRange, 2);
		assertEquals(ret, new Range(-10, 10));
	}

	// -----------------------------------------------------------
	// isNaNRange()

	// tests NaN range detection, should only provide true when both ends are NaN
	@Test
	public void testIsNaNRange(){
		assertFalse(exampleRange.isNaNRange());

		Range testRange = new Range(Double.NaN, Double.NaN);
		assertTrue(testRange.isNaNRange());

		testRange = new Range(Double.NaN, 5);
		assertFalse(testRange.isNaNRange());

		testRange = new Range(-5, Double.NaN);
		assertFalse(testRange.isNaNRange());
	}
	
	// -----------------------------------------------------------
	// Misc tests

	// tests the constructor for throwing an exception when lower > upper
	@Test
	public void testConstructorException() throws IllegalArgumentException {
		exception.expect(IllegalArgumentException.class);
		Range test = new Range(5, -1);
	}
	
	// tests the hashing function, hash was calculated before hand and used 
	// code as a guide to determining the hash
	@Test
	public void testHash() {
		assertEquals(exampleRange.hashCode(), 39321600);
	}

	// tests the equals method on various objects and ranges
	@Test
	public void testEquals() {
		Double a = 5.0;
		assertFalse(exampleRange.equals(a));
		
		Range testRange = new Range(-5, 5);
		assertTrue(exampleRange.equals(testRange));
		
		testRange = new Range(-4, 5);
		assertFalse(exampleRange.equals(testRange));
		
		testRange = new Range(-5, 4);
		assertFalse(exampleRange.equals(testRange));
	}
	
	
	@After
	public void tearDown() throws Exception {exampleRange = null;}
}
