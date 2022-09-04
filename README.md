### JVTT - Java Virtual Thread Test

Goal of the VirtualThreadTest (VTT) is to compare Java Virtual Threads and Java Native Threads in aspect of
the time taken to invoke, run and complete a Sample Job (Which sleeps for a second)

We use two kinds of executor services:

 - Native Fixed thread pool service (`Executors.newFixedThreadPool`)
 - Virtual Thread executor service (`Executors.newVirtualThreadPerTaskExecutor`)
 
We select the thread pool based on env var `USE_VIRTUAL`.

We are not switching the thread pool in the same run as JIT might have made the application hot and results might
 be favourable for the later executor we run
 
We use the `--enable-preview` option as virtual threads are part of preview release of JDK 19


### Settings
By Default the program uses the following settings, but can be changed with the appropriate env vars `NUM_JOBS`, `NUM_NATIVE_THREADS`

*Defaults*
```
NUM_JOBS = 10000
NUM_NATIVE_THREADS = 100
```

*Limits*
```
0 < NUM_JOBS <= 100000
0 < NUM_NATIVE_THREADS <= 100
```
### Run

You can run the `run_test.sh` script directly or run the docker container with customised settings (Threads and Jobs)

##### Run Script
```
git clone https://github.com/bharathappali/jvtt
cd jvtt
./run_test.sh
```

##### Run docker image

*Run with native threads*

```
docker run --rm \
-e NUM_NATIVE_THREAD=100 \
-e NUM_JOBS=10000 \
-it bharathappali/jvtt:latest
```

*Run with virtual threads*

```
docker run --rm \
-e USE_VIRTUAL="true" \
-e NUM_JOBS=10000 \
-it bharathappali/jvtt:latest
```
