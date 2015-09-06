package info.izumin.java;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by izumin on 9/7/15.
 */
public class SimpleEventEmitterTest {
    enum Dummy { EVENT1, EVENT2, EVENT3 }

    @Mock private SimpleEventListener<String> spyListener1, spyListener2, spyListener3;
    private SimpleEventEmitter<Dummy, String> mEmitter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mEmitter = new SimpleEventEmitter<>(Dummy.class);
    }

    @Test
    public void runCallbacksForOnlyRegisteredListeners() throws Exception {
        final String value = "test data";
        mEmitter.on(Dummy.EVENT1, spyListener1);
        mEmitter.on(Dummy.EVENT2, spyListener2);
        mEmitter.on(Dummy.EVENT3, spyListener3);
        mEmitter.emit(Dummy.EVENT2, value);
        verify(spyListener1, never()).run(value);
        verify(spyListener2, times(1)).run(value);
        verify(spyListener3, never()).run(value);
    }

    @Test
    public void runCallbacksForMultipleListeners() throws Exception {
        final String value = "test data";
        mEmitter.on(Dummy.EVENT3, spyListener1);
        mEmitter.on(Dummy.EVENT3, spyListener2);
        mEmitter.on(Dummy.EVENT3, spyListener3);
        mEmitter.emit(Dummy.EVENT3, value);
        verify(spyListener1, times(1)).run(value);
        verify(spyListener2, times(1)).run(value);
        verify(spyListener3, times(1)).run(value);
    }

    @Test
    public void runCallbacksMultipleTimesForTheSameListener() throws Exception {
        final String value = "test data";
        mEmitter.on(Dummy.EVENT1, spyListener3);
        mEmitter.on(Dummy.EVENT1, spyListener3);
        mEmitter.on(Dummy.EVENT1, spyListener3);
        mEmitter.emit(Dummy.EVENT1, value);
        verify(spyListener1, never()).run(value);
        verify(spyListener2, never()).run(value);
        verify(spyListener3, times(3)).run(value);
    }
}
