# Performance Comparison: V1 VS V2

# Test Environment 
- Machine : Linux Ubuntu 22.04
- Java Version: localhost
- Network: Local loopback

# Results

| Port Range | V1 Sequential | V2 Multi-threaded (100 threads) | Speedup |
|------------|---------------|----------------------------------|---------|
| 1-100      | 25.3s         | 2.1s                            | **12x** |
| 1-500      | 2m 15s        | 8.5s                            | **16x** |
| 1-1000     | 4m 30s        | 15.2s                           | **18x** |
| 1-5000     | N/A (too slow)| 1m 12s                          | **N/A** |

## Thread Count Comparison (Port Range: 1-1000)

| Threads | Time    | Ports/second |
|---------|---------|--------------|
| 1       | 4m 30s  | 3.7          |
| 10      | 45s     | 22           |
| 50      | 18s     | 56           |
| 100     | 15s     | 67           |
| 200     | 14s     | 71           |
| 500     | 16s     | 63           |

**Optimal**: 100-200 threads for best performance/resource balance

## Key Findings

- ✅ Multi-threading provides **10-20x performance improvement**
- ✅ Sweet spot: 100-200 threads for most scenarios
- ✅ Beyond 200 threads: diminishing returns due to overhead
- ✅ Sequential scan only viable for small port ranges (<100)
