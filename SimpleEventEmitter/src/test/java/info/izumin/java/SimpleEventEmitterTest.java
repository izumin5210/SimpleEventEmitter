package info.izumin.java;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by izumin on 9/7/15.
 */
public class SimpleEventEmitterTest {
    enum Dummy { EVENT1, EVENT2, EVENT3 }

    @Mock private SimpleEventListener<String> spyListener1, spyListener2, spyListener3;
    private SimpleEventEmitter<Dummy> mEmitter;
    private EnumMap<Dummy, Map<Class, List<String>>> mSpyEnumMap;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mEmitter = new SimpleEventEmitter<>(Dummy.class);
        mSpyEnumMap = spy(new EnumMap<Dummy, Map<Class, List<String>>>(Dummy.class));
        Whitebox.setInternalState(mEmitter, "mListeners", mSpyEnumMap);
    }

    @Test
    public void callOnWhenTheFirstTimeForTheEvent() {
        mEmitter.on(Dummy.EVENT1, String.class, spyListener1);
        verify(mSpyEnumMap, times(1)).put(eq(Dummy.EVENT1), anyMap());
        assertThat(mSpyEnumMap.get(Dummy.EVENT1).get(String.class)).hasSize(1);
    }

    @Test
    public void addListenerDelegateToOn() throws Exception {
        mEmitter = spy(mEmitter);
        mEmitter.addListener(Dummy.EVENT1, String.class, spyListener1);
        verify(mEmitter, times(1)).on(Dummy.EVENT1, String.class, spyListener1);
    }

    @Test
    public void runCallbacksForOnlyRegisteredListeners() throws Exception {
        final String value = "test data";
        mEmitter.on(Dummy.EVENT1, String.class, spyListener1);
        mEmitter.on(Dummy.EVENT2, String.class, spyListener2);
        mEmitter.on(Dummy.EVENT3, String.class, spyListener3);
        mEmitter.emit(Dummy.EVENT2, String.class, value);
        verify(spyListener1, never()).run(value);
        verify(spyListener2, times(1)).run(value);
        verify(spyListener3, never()).run(value);
    }

    @Test
    public void runCallbacksForMultipleListeners() throws Exception {
        final String value = "test data";
        mEmitter.on(Dummy.EVENT3, String.class, spyListener1);
        mEmitter.on(Dummy.EVENT3, String.class, spyListener2);
        mEmitter.on(Dummy.EVENT3, String.class, spyListener3);
        mEmitter.emit(Dummy.EVENT3, String.class, value);
        verify(spyListener1, times(1)).run(value);
        verify(spyListener2, times(1)).run(value);
        verify(spyListener3, times(1)).run(value);
    }

    @Test
    public void runCallbacksMultipleTimesForTheSameListener() throws Exception {
        final String value = "test data";
        mEmitter.on(Dummy.EVENT1, String.class, spyListener3);
        mEmitter.on(Dummy.EVENT1, String.class, spyListener3);
        mEmitter.on(Dummy.EVENT1, String.class, spyListener3);
        mEmitter.emit(Dummy.EVENT1, String.class, value);
        verify(spyListener1, never()).run(value);
        verify(spyListener2, never()).run(value);
        verify(spyListener3, times(3)).run(value);
    }

    @Test
    public void countListenersIncludingRepetitionListeners() throws Exception {
        mEmitter.on(Dummy.EVENT1, String.class, spyListener1);
        mEmitter.on(Dummy.EVENT2, String.class, spyListener2);
        mEmitter.on(Dummy.EVENT1, String.class, spyListener3);
        mEmitter.on(Dummy.EVENT1, String.class, spyListener3);
        assertThat(mEmitter.listenersCount(Dummy.EVENT1, String.class)).isEqualTo(3);
    }

    @Test
    public void countListenersReturn0WhenListenersIsEmpty() throws Exception {
        assertThat(mEmitter.listenersCount(Dummy.EVENT1, String.class)).isEqualTo(0);
    }

    @Test
    public void removeListenerAboutSpecificListener() throws Exception {
        mEmitter.on(Dummy.EVENT1, String.class, spyListener1);
        mEmitter.on(Dummy.EVENT2, String.class, spyListener2);
        mEmitter.on(Dummy.EVENT1, String.class, spyListener3);
        mEmitter.on(Dummy.EVENT1, String.class, spyListener3);
        mEmitter.removeListener(Dummy.EVENT1, String.class, spyListener3);
        assertThat(mEmitter.listenersCount(Dummy.EVENT1, String.class)).isEqualTo(2);
    }

    @Test
    public void remoteAllListenersForSpecificEvent() throws Exception {
        mEmitter.on(Dummy.EVENT1, String.class, spyListener1);
        mEmitter.on(Dummy.EVENT2, String.class, spyListener2);
        mEmitter.on(Dummy.EVENT1, String.class, spyListener3);
        mEmitter.on(Dummy.EVENT1, String.class, spyListener3);
        mEmitter.removeAllListeners(Dummy.EVENT1, String.class);
        assertThat(mEmitter.listenersCount(Dummy.EVENT1, String.class)).isEqualTo(0);
        assertThat(mEmitter.listenersCount(Dummy.EVENT2, String.class)).isNotEqualTo(0);
    }

    @Test
    public void remoteAllListenersForAllEvents() throws Exception {
        mEmitter.on(Dummy.EVENT1, String.class, spyListener1);
        mEmitter.on(Dummy.EVENT2, String.class, spyListener2);
        mEmitter.on(Dummy.EVENT1, String.class, spyListener3);
        mEmitter.on(Dummy.EVENT1, String.class, spyListener3);
        mEmitter.removeAllListeners();
        assertThat(mEmitter.listenersCount(Dummy.EVENT1, String.class)).isEqualTo(0);
        assertThat(mEmitter.listenersCount(Dummy.EVENT2, String.class)).isEqualTo(0);
        assertThat(mEmitter.listenersCount(Dummy.EVENT3, String.class)).isEqualTo(0);
    }
}
