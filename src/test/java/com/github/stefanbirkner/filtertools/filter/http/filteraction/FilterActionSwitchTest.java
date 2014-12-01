package com.github.stefanbirkner.filtertools.filter.http.filteraction;

import com.github.stefanbirkner.filtertools.filter.Predicate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.stefanbirkner.filtertools.filter.http.filteraction.FilterActionSwitch.execute;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.*;

public class FilterActionSwitchTest {
    private static final FilterConfig DUMMY_FILTER_CONFIG = mock(FilterConfig.class);
    private static final HttpServletRequest DUMMY_REQUEST = mock(HttpServletRequest.class);
    private static final HttpServletResponse DUMMY_RESPONSE = mock(HttpServletResponse.class);
    private final FilterAction actionForSuccessfulTest = mock(FilterAction.class);
    private final FilterAction actionForFailingTest = mock(FilterAction.class);
    private final Predicate<HttpServletRequest> predicate = mock(Predicate.class);
    private final FilterAction theSwitch
        = execute(actionForSuccessfulTest).when(predicate).otherwiseExecute(actionForFailingTest);

    @Rule
    public final ExpectedException thrown = none();

    @Test
    public void executesFirstActionIfPredicateMatches() throws Exception {
        when(predicate.test(DUMMY_REQUEST)).thenReturn(true);
        theSwitch.execute(DUMMY_REQUEST, DUMMY_RESPONSE);
        verify(actionForSuccessfulTest).execute(DUMMY_REQUEST, DUMMY_RESPONSE);
        verifyZeroInteractions(actionForFailingTest);
    }

    @Test
    public void executesFirstActionIfPredicateDoesNotMatch() throws Exception {
        when(predicate.test(DUMMY_REQUEST)).thenReturn(false);
        theSwitch.execute(DUMMY_REQUEST, DUMMY_RESPONSE);
        verifyZeroInteractions(actionForSuccessfulTest);
        verify(actionForFailingTest).execute(DUMMY_REQUEST, DUMMY_RESPONSE);
    }

    @Test
    public void cannotBeCreatedWithoutActionForSuccessfulTest() {
        thrown.expect(NullPointerException.class);
        execute(null);
    }

    @Test
    public void cannotBeCreatedWithoutPredicate() {
        thrown.expect(NullPointerException.class);
        execute(actionForSuccessfulTest).when(null);
    }

    @Test
    public void cannotBeCreatedWithoutActionForFailingTest() {
        thrown.expect(NullPointerException.class);
        execute(actionForSuccessfulTest).when(predicate).otherwiseExecute(null);
    }

    @Test
    public void initializesEveryAction() throws Exception {
        theSwitch.init(DUMMY_FILTER_CONFIG);
        verify(actionForSuccessfulTest).init(DUMMY_FILTER_CONFIG);
        verify(actionForFailingTest).init(DUMMY_FILTER_CONFIG);
    }

    @Test
    public void destroysEveryAction() throws Exception {
        theSwitch.destroy();
        verify(actionForSuccessfulTest).destroy();
        verify(actionForFailingTest).destroy();
    }

    @Test
    public void destroyActionForSuccessfulTestEvenIfItFailsToDestroyTheOtherAction() {
        doThrow(new RuntimeException()).when(actionForFailingTest).destroy();
        try {
            theSwitch.destroy();
        } catch (RuntimeException e) {
            verify(actionForSuccessfulTest).destroy();
        }
    }

    @Test
    public void destroyActionForFailingTestEvenIfItFailsToDestroyTheOtherAction() {
        doThrow(new RuntimeException()).when(actionForSuccessfulTest).destroy();
        try {
            theSwitch.destroy();
        } catch (RuntimeException e) {
            verify(actionForFailingTest).destroy();
        }
    }
}
