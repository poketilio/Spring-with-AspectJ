package com.example.aspectj;

import com.example.beans.User;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AspectTests {
    @Test
    public void testDateTimeToString() {
        assertThat(new DateTime().toString(), is(DateTimeToStringAspect.TO_STRING_RESULT));
    }

    @Test
    public void testUserGetName() {
        User user = new User();
        user.setName("Name");
        assertThat(user.getName(), is(UserAspect.TO_STRING_RESULT));
    }
}
