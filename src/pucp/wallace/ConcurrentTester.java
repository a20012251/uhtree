package pucp.wallace;

import java.util.Random;

public class ConcurrentTester {
	static final int workers = 10000;
	static final int numThreads = 4;
	static final int outerPasses = workers / numThreads;
    static final int innerOps = 5000;
    static final int putPct = 30;
    static final int searchPct = 90;
    static final int order = 4;

	private static int getHash(int x, int bits) {
		int mask = (int)((1L << (1 << bits)) - 1);
		return x & mask;
	}

	public static void main(String[] args) {
		ConcurrentTester tester = new ConcurrentTester();
		tester.testConcurrent();
	}
	
    public void testConcurrent() {
        final UHTree map = UHTreeCreator.getNewUHTree(order);
        final Random random = new Random(0);
        final int keyRange = 10000;
		final RandomDistribution.Zipf zipf = new RandomDistribution.Zipf(random, 0, keyRange, 1/0.9);
	
		long t1 = System.nanoTime();
        for (int outer = 0; outer < outerPasses; ++outer) {
            ParUtil.parallel(numThreads, new Runnable() {

                public void run() {
                    for (int inner = 0; inner < innerOps; ++inner) {
                        final int pct = random.nextInt(100);
                        final int key = zipf.nextInt();
                        final int hash = getHash(key, order);
                        if (pct < putPct) {
                            map.add(hash, key);
                        } else if(pct < searchPct) {
                        	map.contains(hash, key);
                        } else {
                            map.contains(hash, key);
                        }
                    }
                    
                    System.out.println("Finished pass.");
                }
            });
        }
        long t2 = System.nanoTime();
        System.out.println((t2 - t1) / (1000000000.0));
        for (int i = 0; i < keyRange; i++)
        	map.remove(getHash(i, order), i);
        assert map.isEmpty();
    }
}
