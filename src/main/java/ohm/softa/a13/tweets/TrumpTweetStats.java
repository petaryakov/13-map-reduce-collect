package ohm.softa.a13.tweets;

import ohm.softa.a13.model.Tweet;
import org.apache.commons.lang3.NotImplementedException;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Peter Kurfer
 */
public class TrumpTweetStats {

	private static final Pattern ALPHA_NUMERICAL = Pattern.compile("[a-z0-9@]+");

	public static Map<String, Long> calculateSourceAppStats(Stream<Tweet> tweetStream) {
        /* TODO group the tweets by the `sourceApp` they were created with and count how many it were per `sourceApp` */
        //throw new NotImplementedException("TrumpTweetStats.calculateSourceAppStats(...) not implemented yet.");
		return tweetStream
			/* transform every Tweet object to the plain source app name */
			.map(Tweet::getSourceApp)
			/* collect the tweets in a map of String and Integer - short form of map-reduce */
			.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    }

    public static Map<String, Set<Tweet>> calculateTweetsBySourceApp(Stream<Tweet> tweetStream) {
        /* TODO group the tweets by the `sourceApp`
         * collect the tweets in `Set`s for each source app */
       // throw new NotImplementedException("TrumpTweetStats.calculateTweetsBySourceApp(...) not implemented yet.");
		return tweetStream
			/* collect with default `groupingBy` but replace the value collector */
			.collect(Collectors.groupingBy(Tweet::getSourceApp, Collectors.toSet()));
    }

    public static Map<String, Integer> calculateWordCount(Stream<Tweet> tweetStream, List<String> stopWords) {
        /* Remark: implement this method at last */
        /* TODO split the tweets, lower them, trim them, remove all words that are in the `stopWords`,
         * reduce the result to a Map<String, Integer> to count how often each word were in the tweets
         * optionally you could filter for all words that were used more than 10 times */
        //throw new NotImplementedException("TrumpTweetStats.tweetStream(...) not implemented yet.");
		// -> current type Stream<Tweet>
		return tweetStream
			// extract the tweet text -> current type Stream<String>
			.map(Tweet::getText)
			// split tweet text at ' ' -> current type Stream<String[]>
			.map(t -> t.split("( )+"))
			// use `flatMap` to map the arrays to a single Stream -> current type Stream<String>
			.flatMap(Arrays::stream)
			// map all Strings to lowercase -> current type Stream<String>
			.map(String::toLowerCase)
			// filter for only alpha numerical words -> current type Stream<String>
			.filter(word -> ALPHA_NUMERICAL.matcher(word).matches())
			// filter to remove stop words -> current type Stream<String>
			.filter(w -> stopWords.stream().noneMatch(sw -> sw.equals(w)))
			// reduce the Stream to Map<String, Integer>
			.reduce(new LinkedHashMap<String, Integer>(), (acc, word) -> {
				/* put the current word to the map - overwrites entry but computes new value first
				 * by calling `.compute` on the map that passes the current value or null to the lambda depending if the key already exists or not */
				acc.put(word, acc.compute(word, (k, v) -> v == null ? 1 : v + 1));
				return acc;
			}, (m1, m2) -> m1) /* Map<String, Long> */
			/* get entry set of the resulting map and create a new Stream */
			.entrySet()
			.stream()
			/* filter for words that occurred less than 10 times */
			.filter(entry -> entry.getValue() > 10)
			/* collect them once more in a map */
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
