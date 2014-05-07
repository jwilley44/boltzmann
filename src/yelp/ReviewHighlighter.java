package yelp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReviewHighlighter
{
	private static final String kHighlightTag = "[[HIGHLIGHT]]";
	private static final String kEndHighlightTag = "[[ENDHIGHLIGHT]]";

	static String highlight(String pReview, String pQuery)
	{
		String vRegex = buildRegex(pQuery);
		Pattern vPattern = Pattern.compile(vRegex);
		Matcher vMatcher = vPattern.matcher(pReview);
		return collapseHighlightTags(vMatcher.replaceAll(kHighlightTag + "$1"
				+ kEndHighlightTag));
	}

	public static String getRelevant(String pReview, String pQuery)
	{
		List<Integer> vIndexes = new ArrayList<Integer>();
		String vLowercase = pReview.toLowerCase();
		for (String vTerm : pQuery.split(" "))
		{
			int vIndex = vLowercase.indexOf(vTerm.toLowerCase());
			if (vIndex > -1)
			{
				vIndexes.add(1 + indexOfLastPunctuation(
						vLowercase.substring(0, vIndex), 0));
			}
		}
		Collections.sort(vIndexes);
		StringBuilder vSnippet = new StringBuilder();
		int vLastIndex = -1;
		for (Integer vIndex : vIndexes)
		{
			if (vIndex.intValue() > vLastIndex)
			{
				if (vIndex.intValue() - vLastIndex != 1)
				{
					vSnippet.append("..");
				}
				vLastIndex = indexOfFirstPunctuation(pReview, vIndex.intValue()) + 1;
				vSnippet.append(pReview.substring(vIndex.intValue(), vLastIndex));
			}
		}
		return highlight(vSnippet.toString().trim(), pQuery);
	}

	private static int indexOfFirstPunctuation(String pString, int pFrom)
	{
		int vPeriod = pString.indexOf(".", pFrom);
		int vQuestion = pString.indexOf("?", pFrom);
		int vBang = pString.indexOf("!", pFrom);
		return Math.min(vPeriod < 0 ? pString.length() : vPeriod, Math.min(
				vQuestion < 0 ? pString.length() : vQuestion,
				vBang < 0 ? pString.length() : vBang));
	}

	private static int indexOfLastPunctuation(String pString, int pFrom)
	{
		int vPeriod = pString.lastIndexOf(".");
		int vQuestion = pString.lastIndexOf("?");
		int vBang = pString.lastIndexOf("!");
		return Math.max(vPeriod, Math.max(
				vQuestion,
				vBang));
	}

	private static String buildRegex(String pQuery)
	{
		StringBuilder vBuilder = new StringBuilder();
		for (String vTerm : pQuery.split(" "))
		{
			vBuilder.append("|" + decorate(ignoreCase(vTerm)));
		}
		return "(" + vBuilder.toString().replaceFirst("\\|", "") + ")";
	}

	private static String ignoreCase(String pTerm)
	{
		String vFirstLetter = pTerm.substring(0, 1).toLowerCase();
		return "(" + vFirstLetter.toUpperCase() + "|" + vFirstLetter + ")"
				+ pTerm.substring(1);
	}

	/*
	 * I deliberately chose to highlight whole words that contain the search
	 * terms, since I think that is more helpful when looking at searches. This
	 * method could be made more robust to so that strawberries is highlighted
	 * when strawberry is a search term
	 */
	private static String decorate(String pTerm)
	{
		return "\\w*" + pTerm + "\\w*";
	}

	private static String collapseHighlightTags(String pHighlighted)
	{
		return pHighlighted
				.replace(kEndHighlightTag + " " + kHighlightTag, " ");
	}
}
