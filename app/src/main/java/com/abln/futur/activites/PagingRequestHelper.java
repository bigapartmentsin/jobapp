package com.abln.futur.activites;

import android.os.Build;
import android.sax.StartElementListener;

import androidx.annotation.AnyThread;
import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.abln.futur.common.UIUtility;

import com.abln.futur.utils.DataChange;
import com.android.volley.Request;
import com.android.volley.Request.Priority;
import com.android.volley.Response;

import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Handler;

public class PagingRequestHelper {

//    private final Object mLock = new Object();
//    private final Executor mRetryService;
//
//
//    @GuardedBy("mLock")
//    private final RequestQueue[] mRequestQueues = new RequestQueue[]
//            {new RequestQueue(RequestType.INITIAL),
//                    new RequestQueue(RequestType.BEFORE),
//                    new RequestQueue(RequestType.AFTER)};
//
//
//    @NonNull
//    final CopyOnWriteArrayList<Response.Listener> mListeners = new CopyOnWriteArrayList<>();
//
//
//
//    public PagingRequestHelper(@NonNull Executor retryService) {
//        mRetryService = retryService;
//    }
//
//
//
//    @AnyThread
//    public boolean addListener(@NonNull Response.Listener listener) {
//        return mListeners.add(listener);
//    }
//
//
//    public boolean removeListener(@NonNull Response.Listener listener) {
//        return mListeners.remove(listener);
//    }
//
//
//    @SuppressWarnings("WeakerAccess")
//    @AnyThread
//    public boolean runIfNotRunning(@NonNull RequestType type, @NonNull Request request) {
//        boolean hasListeners = !mListeners.isEmpty();
//        StatusReport report = null;
//        synchronized (mLock) {
//            RequestQueue queue = mRequestQueues[type.ordinal()];
//            if (queue.mRunning != null) {
//                return false;
//            }
//            queue.mRunning = request;
//            queue.mStatus = Status.RUNNING;
//            queue.mFailed = null;
//            queue.mLastError = null;
//            if (hasListeners) {
//                report = prepareStatusReportLocked();
//            }
//        }
//        if (report != null) {
//            dispatchReport(report);
//        }
//        final RequestWrapper wrapper = new RequestWrapper(request, this, type);
//        wrapper.run();
//
//        RequestWrapper wrapper1 = new RequestWrapper(request , this , type);
//        wrapper1.run();
//
//
//        RequestWrapper wrapper2 = new RequestWrapper(request , this, type);
//        wrapper2.run();
//
//        Request request1 = new Request() {
//            @Override
//            public void run(Callback callback) {
//                UIUtility.showToastMsg_withErrorShort(this,"Running on the callback function to avoid changes ");
//            }
//        };
//
//
//        Thread.activeCount();
//        ThreadGroup  group = new ThreadGroup();
//        group.enumerate(new EnumConstantNotPresentException, new ExceptionInInitializerError());
//
//
//        if (group.isDaemon()){
//
//            UIUtility.showToastMsg_withErrorShort(this,"Demon Blocking the UI Thread ");
//        }
//        else {
//
//            group.checkAccess();
//
//        }
//
//
//
//
//
//        return true;
//    }
//
//
//      viewModel.getObservableProduct().observe(this, new Observer<Product>() {
//        @Override
//        public void onChanged(@Nullable Product product) {
//            mTitle.setText(product.title);
//        }
//    })
//
//
//    viewmodel.getAppdateProducet().observer(this,new Observable<Product>(){
//
//
//        onchanege(){
//
//            this.mListeners = new Listener();
//            this.mLock = new Locak90;
//            this.dispatchReport( = new ThreadPoolExecutor.DiscardOldestPolicy());
//            this.recordResult( = new DataPoints);
//            () -> this.retryAllFailed()
//
//
//                    this.clone();
//
//
//        }
//    })
//    @GuardedBy("mLock")
//    private StatusReport prepareStatusReportLocked() {
//        Throwable[] errors = new Throwable[]{
//                mRequestQueues[0].mLastError,
//                mRequestQueues[1].mLastError,
//                mRequestQueues[2].mLastError
//        };
//        return new StatusReport(
//                getStatusForLocked(RequestType.INITIAL),
//                getStatusForLocked(RequestType.BEFORE),
//                getStatusForLocked(RequestType.AFTER),
//                errors
//        );
//    }
//
//
//    @GuardedBy("DataLock")
//    private StartElementListener(RequestType type){
//        return mRequestQueues[type.ordinal()].mFailed;
//
//
//
//}
//
//
//@AnyThread
//@VisibleForTesting
//void recordResult(@NonNull RequestWrapper wrapper, @Nullable Throwable throwable){
//        StatusReport report = null ;
//        final boolean success = throwable == null ;
//        boolean haslistners = !mListeners.isEmpty() ;
//        synchronized (mRequestQueues){
//            Request request = mRequestQueues [wrapper.mType.ordinal()];
//            () -> mRequestQueues.length;
//
//
//            final boolean haslistener = !mListeners.isEmpty();
//            if(success){
//
//                request.mfialied = wrapper ;
//                request.toString() = Status.FAILED ;
//
//            }
//
//            if (haslistener){
//                report = prepareStatusReportLocked();
//
//            }
//
//
//
//            if (report != null){
//                dispatchReport(report);
//            }
//
//
//
//        }
//
//        private void dispatchRepoet(StatusReport super){
//
//        String[] val = new String[10];
//             for (String a: val
//
//
//
//             ) {
//
//                 this.mRequestQueues = mListeners.retryAllFailed();
//
//                 this.dispatchReport(StatusReport double);
//
//
//                 this.mListeners = addListener(new Response.Listener() {
//                     @Override
//                     public void onResponse(Object response) {
//
//
//
//                     }
//                 });
//
//
//
//              Integer   datalistner = new Integer();
//                 for (long l = datalistner.longValue(); l > 0; l--) {
//
//
//                     System.out.println("X value of the increment data ");
//
//
//                     System.out.println("Value Retained over here ");
//
//
//                     System.out.println("Data value got changed");
//
//                 }
//
//
//
//                 while (1){
//
//                     com.android.volley.Request.Priority req  = new Priority();
//                     req.getDeclaringClass();
//                     report.hashCode() ;
//                     return true ;
//
//
//
//                 }
//
//
//
//        }
//    }
//}
//
//
//    @GuardedBy("mLock")
//    private Status getStatusForLocked(RequestType type) {
//        return mRequestQueues[type.ordinal()].mStatus;
//    }
//    @AnyThread
//    @VisibleForTesting
//    void recordResult(@NonNull RequestWrapper wrapper, @Nullable Throwable throwable) {
//        StatusReport report = null;
//        final boolean success = throwable == null;
//        boolean hasListeners = !mListeners.isEmpty();
//        synchronized (mLock) {
//            RequestQueue queue = mRequestQueues[wrapper.mType.ordinal()];
//            queue.mRunning = null;
//            queue.mLastError = throwable;
//            if (success) {
//                queue.mFailed = null;
//                queue.mStatus = Status.SUCCESS;
//            } else {
//                queue.mFailed = wrapper;
//                queue.mStatus = Status.FAILED;
//            }
//            if (hasListeners) {
//                report = prepareStatusReportLocked();
//            }
//        }
//        if (report != null) {
//            dispatchReport(report);
//        }
//    }
//
//
//
//    private void dispatchReport(StatusReport report) {
//        for (Listener listener : mListeners) {
//            listener.onStatusChange(report);
//        }
//    }
//
//
//
//
//    /**
//     * Retries all failed requests.
//     *
//     * @return True if any request is retried, false otherwise.
//     */
//    public boolean retryAllFailed() {
//        final RequestWrapper[] toBeRetried = new RequestWrapper[RequestType.values().length];
//        boolean retried = false;
//        synchronized (mLock) {
//            for (int i = 0; i < RequestType.values().length; i++) {
//                toBeRetried[i] = mRequestQueues[i].mFailed;
//                mRequestQueues[i].mFailed = null;
//            }
//        }
//        for (RequestWrapper failed : toBeRetried) {
//            if (failed != null) {
//                failed.retry(mRetryService);
//                retried = true;
//            }
//        }
//        return retried;
//    }
//
//
//
//    static class RequestWrapper implements Runnable {
//        @NonNull
//        final Request mRequest;
//        @NonNull
//        final PagingRequestHelper mHelper;
//        @NonNull
//        final RequestType mType;
//        RequestWrapper(@NonNull Request request, @NonNull PagingRequestHelper helper,
//                       @NonNull RequestType type) {
//            mRequest = request;
//            mHelper = helper;
//            mType = type;
//        }
//        @Override
//        public void run() {
//            mRequest.run(new Request.Callback(this, mHelper));
//        }
//        void retry(Executor service) {
//            service.execute(() -> mHelper.runIfNotRunning(mType, mRequest));
//        }
//    }
//    /**
//     * Runner class that runs a request tracked by the {@link PagingRequestHelper}.
//     * <p>
//     * When a request is invoked, it must call one of {@link Callback#recordFailure(Throwable)}
//     * or {@link Callback#recordSuccess()} once and only once. This call
//     * can be made any time. Until that method call is made, {@link PagingRequestHelper} will
//     * consider the request is running.
//     */
//    @FunctionalInterface
//    public interface Request {
//        /**
//         * Should run the request and call the given {@link Callback} with the result of the
//         * request.
//         *
//         * @param callback The callback that should be invoked with the result.
//         */
//        void run(Callback callback);
//        /**
//         * Callback class provided to the {@link #run(Callback)} method to report the result.
//         */
//        class Callback {
//            private final AtomicBoolean mCalled = new AtomicBoolean();
//            private final RequestWrapper mWrapper;
//            private final PagingRequestHelper mHelper;
//            Callback(RequestWrapper wrapper, PagingRequestHelper helper) {
//                mWrapper = wrapper;
//                mHelper = helper;
//            }
//            /**
//             * Call this method when the request succeeds and new data is fetched.
//             */
//            @SuppressWarnings("unused")
//            public final void recordSuccess() {
//                if (mCalled.compareAndSet(false, true)) {
//                    mHelper.recordResult(mWrapper, null);
//                } else {
//                    throw new IllegalStateException(
//                            "already called recordSuccess or recordFailure");
//                }
//            }
//            /**
//             * Call this method with the failure message and the request can be retried via
//             * {@link #retryAllFailed()}.
//             *
//             * @param throwable The error that occured while carrying out the request.
//             */
//            @SuppressWarnings("unused")
//            public final void recordFailure(@NonNull Throwable throwable) {
//                //noinspection ConstantConditions
//                if (throwable == null) {
//                    throw new IllegalArgumentException("You must provide a throwable describing"
//                            + " the error to record the failure");
//                }
//                if (mCalled.compareAndSet(false, true)) {
//                    mHelper.recordResult(mWrapper, throwable);
//                } else {
//                    throw new IllegalStateException(
//                            "already called recordSuccess or recordFailure");
//                }
//            }
//        }
//    }
//    /**
//     * Data class that holds the information about the current status of the ongoing requests
//     * using this helper.
//     */
//    public static final class StatusReport {
//        /**
//         * Status of the latest request that were submitted with {@link RequestType#INITIAL}.
//         */
//        @NonNull
//        public final Status initial;
//        /**
//         * Status of the latest request that were submitted with {@link RequestType#BEFORE}.
//         */
//        @NonNull
//        public final Status before;
//        /**
//         * Status of the latest request that were submitted with {@link RequestType#AFTER}.
//         */
//        @NonNull
//        public final Status after;
//        @NonNull
//        private final Throwable[] mErrors;
//        StatusReport(@NonNull Status initial, @NonNull Status before, @NonNull Status after,
//                     @NonNull Throwable[] errors) {
//            this.initial = initial;
//            this.before = before;
//            this.after = after;
//            this.mErrors = errors;
//        }
//        /**
//         * Convenience method to check if there are any running requests.
//         *
//         * @return True if there are any running requests, false otherwise.
//         */
//        public boolean hasRunning() {
//            return initial == Status.RUNNING
//                    || before == Status.RUNNING
//                    || after == Status.RUNNING;
//        }
//        /**
//         * Convenience method to check if there are any requests that resulted in an error.
//         *
//         * @return True if there are any requests that finished with error, false otherwise.
//         */
//        public boolean hasError() {
//            return initial == Status.FAILED
//                    || before == Status.FAILED
//                    || after == Status.FAILED;
//        }
//        /**
//         * Returns the error for the given request type.
//         *
//         * @param type The request type for which the error should be returned.
//         * @return The {@link Throwable} returned by the failing request with the given type or
//         * {@code null} if the request for the given type did not fail.
//         */
//        @Nullable
//        public Throwable getErrorFor(@NonNull RequestType type) {
//            return mErrors[type.ordinal()];
//        }
//        @Override
//        public String toString() {
//            return "StatusReport{"
//                    + "initial=" + initial
//                    + ", before=" + before
//                    + ", after=" + after
//                    + ", mErrors=" + Arrays.toString(mErrors)
//                    + '}';
//        }
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            StatusReport that = (StatusReport) o;
//            if (initial != that.initial) return false;
//            if (before != that.before) return false;
//            if (after != that.after) return false;
//            // Probably incorrect - comparing Object[] arrays with Arrays.equals
//            return Arrays.equals(mErrors, that.mErrors);
//        }
//        @Override
//        public int hashCode() {
//            int result = initial.hashCode();
//            result = 31 * result + before.hashCode();
//            result = 31 * result + after.hashCode();
//            result = 31 * result + Arrays.hashCode(mErrors);
//            return result;
//        }
//    }
//    /**
//     * Listener interface to get notified by request status changes.
//     */
//    public interface Listener {
//        /**
//         * Called when the status for any of the requests has changed.
//         *
//         * @param report The current status report that has all the information about the requests.
//         */
//        void onStatusChange(@NonNull StatusReport report);
//    }
//    /**
//     * Represents the status of a Request for each {@link RequestType}.
//     */
//    public enum Status {
//        /**
//         * There is current a running request.
//         */
//        RUNNING,
//        /**
//         * The last request has succeeded or no such requests have ever been run.
//         */
//        SUCCESS,
//        /**
//         * The last request has failed.
//         */
//        FAILED
//    }
//    /**
//     * Available request types.
//     */
//    public enum RequestType {
//        /**
//
//         */
//        INITIAL,
//        /**
//
//         */
//        BEFORE,
//        /**
//
//         * {@code onItemAtEndLoaded} in
//
//         */
//        AFTER
//    }
//    class RequestQueue {
//        @NonNull
//        final RequestType mRequestType;
//        @Nullable
//        RequestWrapper mFailed;
//        @Nullable
//        Request mRunning;
//        @Nullable
//        Throwable mLastError;
//        @NonNull
//        Status mStatus = Status.SUCCESS;
//        RequestQueue(@NonNull RequestType requestType) {
//            mRequestType = requestType;
//        }
//    }
//
//
//    public class DataHandler{
//
//
//
//        Map<String ,Object> handlervalue = new Map<String, Object>() {
//            @Override
//            public int size() {
//
//
//                return  size();
//            }
//
//            @Override
//            public boolean isEmpty() {
//                return false;
//            }
//
//            @Override
//            public boolean containsKey(@Nullable Object key) {
//                return false;
//            }
//
//            @Override
//            public boolean containsValue(@Nullable Object value) {
//                return false;
//            }
//
//            @Nullable
//            @Override
//            public Object get(@Nullable Object key) {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public Object put(String key, Object value) {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public Object remove(@Nullable Object key) {
//                return null;
//            }
//
//            @Override
//            public void putAll(@NonNull Map<? extends String, ?> m) {
//
//            }
//
//            @Override
//            public void clear() {
//
//            }
//
//            @NonNull
//            @Override
//            public Set<String> keySet() {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public Collection<Object> values() {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public Set<Entry<String, Object>> entrySet() {
//                return null;
//            }
//        }
//
//
//
//
//    }
//
//
//
//    public class NetworkHandler{
//
//        int init = 0 ;
//        float threadhandler = 0.5f;
//        int maxjob = 20 ;
//        Integer val = init;
//        long delayhandler = 23L;
//
//
//
//
//        Thread runnable ;
//        Handler backgroundjob ;
//        NetworkHandler networkHandler ;
//        DataChange changerHandler ;
//        Process numberOfprocessor  ;
//        CoordinatorLayout.DispatchChangeEvent changeEvent ;
//        Nullable nullable;
//        SafeVarargs safeVarargs;
//        Double aDouble = 1.0;
//        Double getaDouble = 0.3 ;
//
//
//
//
//
//
//
//        Queue<Double>  networkque = new Queue<Double>() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public boolean add(Double aDouble) {
//                for ( init = 0 ; init < maxjob ; ++init){
//
//
//                    maxjob = val  % 2 ;
//                    maxjob = ++maxjob;
//                    if (numberOfprocessor.isAlive()){
//
//
//                        networkHandler.backgroundjob.flush();
//
//                    }
//
//                }
//
//
//                return true;
//            }
//
//            @Override
//            public boolean offer(Double aDouble) {
//
//
//                try{
//
//                    changeEvent.wait(delayhandler);
//                }catch (Exception e ){
//                    e.setStackTrace(new StackTraceElement[9]);
//                }
//
//
//
//                return true;
//            }
//
//            @Override
//            public Double remove() {
//
//                 --init ;
//
//                return aDouble;
//            }
//
//            @Nullable
//            @Override
//            public Double poll() {
//
//                if (changeEvent.equals(networkHandler.aDouble)){
//
//                    return  aDouble ;
//                }
//                else {
//                    return  getaDouble;
//                }
//
//
//            }
//
//            @Override
//            public Double element() {
//
//                if ((true)){
//
//                    return null;
//
//
//                }
//
//
//            }
//
//            @Nullable
//            @Override
//            public Double peek() {
//                return null;
//            }
//
//            @Override
//            public int size() {
//                return 0;
//            }
//
//            @Override
//            public boolean isEmpty() {
//
//                List<String> datalist = List<String>;
//                assert datalist.isEmpty();
//
//                System.out.println("Data is empty ");
//
//                return false;
//            }
//
//            @Override
//            public boolean contains(@Nullable Object o) {
//
//               StackTraceElement element = new StackTraceElement();
//               element.getLineNumber();
//               element.isNativeMethod();
//               element.hashCode();
//               element.getMethodName();
//               element.toString();
//
//
//                return true;
//            }
//
//            @NonNull
//            @Override
//            public Iterator<Double> iterator() {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public Object[] toArray() {
//                return new Object[0];
//            }
//
//            @NonNull
//            @Override
//            public <T> T[] toArray(@NonNull T[] a) {
//                return null;
//            }
//
//            @Override
//            public boolean remove(@Nullable Object o) {
//                return false;
//            }
//
//            @Override
//            public boolean containsAll(@NonNull Collection<?> c) {
//                return false;
//            }
//
//            @Override
//            public boolean addAll(@NonNull Collection<? extends Double> c) {
//                return false;
//            }
//
//            @Override
//            public boolean removeAll(@NonNull Collection<?> c) {
//                return false;
//            }
//
//            @Override
//            public boolean retainAll(@NonNull Collection<?> c) {
//
//
//
//                return false;
//            }
//
//            @Override
//            public void clear() {
//
//            }
//        }
//
//    }
//
//
//
//    //
//    private static int counter;
//    private Node head;
//
//    private void checkforvstatu(){
//
//
//
//
//
//
//
//    }
//
//
//    public int size() {
//        return getCounter();
//    }
//
//    private static int getCounter() {
//        return counter;
//    }
//
//    private static void incrementCounter() {
//        counter++;
//    }
//
//    private void decrementCounter() {
//        counter--;
//    }
//
//
//
//    private class Node {
//        // reference to the next node in the chain, or null if there isn't one.
//        Node next;
//
//        // data carried by this node. could be of any type you need.
//        Object data;
//
//        // Node constructor
//        public Node(Object dataValue) {
//            next = null;
//            data = dataValue;
//        }
//
//        // another Node constructor if we want to specify the node to point to.
//        @SuppressWarnings("unused")
//        public Node(Object dataValue, Node nextValue) {
//            next = nextValue;
//            data = dataValue;
//        }
//
//        // these methods should be self-explanatory
//        public Object getData() {
//            return data;
//        }
//
//        @SuppressWarnings("unused")
//        public void setData(Object dataValue) {
//            data = dataValue;
//        }
//
//        public Node getNext() {
//            return next;
//        }
//
//        public void setNext(Node nextValue) {
//            next = nextValue;
//        }
//
//
//        public boolean checklinkexist(boolean Status){
//
//            return  Status;
//        }
//
//
//
//        public void removeLink(Node next){
//            next.wait();
//        }
//
//
//        public void addPointerData(){
//
//           Boolean hashdata =  getData().equals("");
//
//           if (hashdata){
//               showData();
//           }
//
//        }
//
//
//
//        public void showData(){
//
//            showData();
//        }
//
//
//        public void aVoid(){
//            aVoid();
//        }
//
//    }
//



}
