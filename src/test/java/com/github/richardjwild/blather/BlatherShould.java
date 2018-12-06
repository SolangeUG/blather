package com.github.richardjwild.blather;

import com.github.richardjwild.blather.application.Application;
import com.github.richardjwild.blather.application.ApplicationBuilder;
import com.github.richardjwild.blather.helper.DatabaseConnection;
import com.github.richardjwild.blather.io.Input;
import com.github.richardjwild.blather.io.Output;
import com.github.richardjwild.blather.time.Clock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;

import static com.github.richardjwild.blather.helper.DataBaseHelper.clearTestData;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BlatherShould {

    private static final long TWO_MINUTES = 120;
    private static final long ONE_MINUTE = 60;
    private static final long FIFTEEN_SECONDS = 15;
    private static final long ONE_SECOND = 1;

    @Mock
    private Input input;

    @Mock
    private Output output;

    @Mock
    private Clock clock;

    private Application application;

    @Before
    public void initialize() {
        application = ApplicationBuilder.build(input, output, clock);
    }

    @Test
    public void display_a_users_posted_messages() {
        when(input.readLine())
                .thenReturn("Alice -> My first message")
                .thenReturn("Bob -> Hello world!")
                .thenReturn("Alice -> Sup everyone?")
                .thenReturn("Bob -> I wanna party :)")
                .thenReturn("Alice")
                .thenReturn("Bob")
                .thenReturn("quit");
        Instant now = Instant.now();
        when(clock.now())
                .thenReturn(now.minusSeconds(TWO_MINUTES))
                .thenReturn(now.minusSeconds(ONE_MINUTE))
                .thenReturn(now.minusSeconds(FIFTEEN_SECONDS))
                .thenReturn(now.minusSeconds(ONE_SECOND))
                .thenReturn(now);

        application.run();

        InOrder inOrder = inOrder(output);
        inOrder.verify(output).writeLine("Welcome to Blather");
        inOrder.verify(output).writeLine("My first message (2 minutes ago)");
        inOrder.verify(output).writeLine("Sup everyone? (15 seconds ago)");
        inOrder.verify(output).writeLine("Hello world! (1 minute ago)");
        inOrder.verify(output).writeLine("I wanna party :) (1 second ago)");
        inOrder.verify(output).writeLine("Bye!");
    }

    @Test
    public void allow_a_user_to_follow_other_users() {
        when(input.readLine())
                .thenReturn("Alice -> My first message")
                .thenReturn("Bob -> Hello world!")
                .thenReturn("Alice -> Sup everyone?")
                .thenReturn("Bob -> I wanna party :)")
                .thenReturn("Emma follows Bob")
                .thenReturn("Emma follows Alice")
                .thenReturn("Emma wall")
                .thenReturn("quit");
        Instant now = Instant.now();
        when(clock.now())
                .thenReturn(now.minusSeconds(TWO_MINUTES))
                .thenReturn(now.minusSeconds(ONE_MINUTE))
                .thenReturn(now.minusSeconds(FIFTEEN_SECONDS))
                .thenReturn(now.minusSeconds(ONE_SECOND))
                .thenReturn(now);

        application.run();

        InOrder inOrder = inOrder(output);
        inOrder.verify(output).writeLine("Welcome to Blather");
        inOrder.verify(output).writeLine("Alice - My first message (2 minutes ago)");
        inOrder.verify(output).writeLine("Bob - Hello world! (1 minute ago)");
        inOrder.verify(output).writeLine("Alice - Sup everyone? (15 seconds ago)");
        inOrder.verify(output).writeLine("Bob - I wanna party :) (1 second ago)");
        inOrder.verify(output).writeLine("Bye!");
    }

    @After
    public void tearDown() {
        clearTestData(new DatabaseConnection().getConnection());
    }
}
