package yelp;

import junit.framework.Assert;

import org.junit.Test;

public class ReviewHighlighterTest
{
	@Test
	public void testHighlight()
	{
		String vReview = "I like fish. Littlestar’s deep dish pizza sure is "
				+ "fantastic. Dogs are funny.";
		Assert.assertEquals("I like fish. Littlestar’s deep dish "
				+ "[[HIGHLIGHT]]pizza[[ENDHIGHLIGHT]] sure is fantastic. "
				+ "Dogs are funny.",
				ReviewHighlighter.highlight(vReview, "pizza"));
	}

	@Test
	public void testGetHighlightedSnippet()
	{
		String vReview = "Cheesepizza is my favorite thing to eat. I like fish."
				+ " Littlestar’s deep dish pizza sure is fantastic. "
				+ "Dogs are funny. I really loved all their pizzas.";
		Assert.assertEquals(
				"[[HIGHLIGHT]]Cheesepizza[[ENDHIGHLIGHT]] is my favorite thing to eat... "
						+ "Littlestar’s [[HIGHLIGHT]]deep dish pizza[[ENDHIGHLIGHT]] sure is fantastic.",
				ReviewHighlighter.getRelevant(vReview, "deep dish pizza"));

		vReview = "The burgers were good, but I did not like the fries. " +
				"The staff was unfriendly? Their shakes however, are awesome!";
		Assert.assertEquals(
				"The [[HIGHLIGHT]]burgers[[ENDHIGHLIGHT]] were good, " +
				"but I did not like the fries.",
				ReviewHighlighter.getRelevant(vReview, "burger soda"));
		System.out.println(ReviewHighlighter.getRelevant(vReview,
				"burger fries shake"));
		Assert.assertEquals(
				"The [[HIGHLIGHT]]burgers[[ENDHIGHLIGHT]] were good, but I did " +
				"not like the [[HIGHLIGHT]]fries[[ENDHIGHLIGHT]]... " +
				"Their [[HIGHLIGHT]]shakes[[ENDHIGHLIGHT]] however, are awesome!",
				ReviewHighlighter.getRelevant(vReview, "Burger fries shake"));
	}
}
