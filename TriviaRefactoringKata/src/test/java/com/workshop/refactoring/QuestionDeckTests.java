package com.workshop.refactoring;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnitParamsRunner.class)
public class QuestionDeckTests {

    @Test
    @Parameters({
            "0,Pop", "4,Pop", "8,Pop",
            "1,Science", "5,Science", "9,Science",
            "2,Sports", "6,Sports", "10,Sports",
            "3,Rock", "7,Rock", "11,Rock",
    })
    public void categoryForPlace(Integer place, String expected) throws Exception {
        final QuestionDeck deck = new QuestionDeck();
        final String category = deck.categoryFor(place);
        assertThat(category, is(expected));
    }

    @Test
    @Parameters({"12", "555", "-1", "" + Integer.MAX_VALUE})
    public void outOfBoardPlace(Integer place) throws Exception {
        final QuestionDeck deck = new QuestionDeck();
        try {
            deck.categoryFor(place);
            fail("expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(OutOfBoardPlaceException.class));
            assertThat(e.getMessage(), containsString(place.toString()));
        }
    }

    @Test
    @Parameters({
            "Pop, Pop Question 0",
            "Science, Science Question 0",
            "Sports, Sports Question 0",
            "Rock, Rock Question 0",
    })
    public void firstQuestion(String category, Object expected) throws Exception {
        final QuestionDeck deck = new QuestionDeck();
        deck.fillQuestions();
        final Object question = deck.askQuestion(category);
        assertThat(question, is(expected));
    }

    @Test
    public void questionForUnknownCategory_() throws Exception {
        final QuestionDeck deck = new QuestionDeck();
        deck.fillQuestions();
        final Object question = deck.askQuestion("unknown");
        assertThat(question, nullValue());
    }

    @Test
    public void questionForUnknownCategory() throws Exception {
        final QuestionDeck deck = new QuestionDeck();
        deck.fillQuestions();
        try {
            deck.askQuestion("boh");
            fail("expected exception to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(UnknownCategoryException.class));
            assertThat(e.getMessage(), containsString("boh"));
        }
    }

    @Test
    @Parameters({
            "Pop",
            "Science",
            "Sports",
            "Rock",
    })
    public void askMultipleQuestionsForSameCategory(String category) throws Exception {
        final QuestionDeck deck = new QuestionDeck();
        deck.fillQuestions();
        assertThat(deck.askQuestion(category), is(category + " Question 0"));
        assertThat(deck.askQuestion(category), is(category + " Question 1"));
        assertThat(deck.askQuestion(category), is(category + " Question 2"));
        assertThat(deck.askQuestion(category), is(category + " Question 3"));
    }

    @Test
    public void askMultipleQuestionsForMixedCategories() throws Exception {
        final QuestionDeck deck = new QuestionDeck();
        deck.fillQuestions();
        assertThat(deck.askQuestion("Pop"), is("Pop Question 0"));
        assertThat(deck.askQuestion("Sports"), is("Sports Question 0"));
        assertThat(deck.askQuestion("Pop"), is("Pop Question 1"));
        assertThat(deck.askQuestion("Sports"), is("Sports Question 1"));
        assertThat(deck.askQuestion("Rock"), is("Rock Question 0"));
    }
}