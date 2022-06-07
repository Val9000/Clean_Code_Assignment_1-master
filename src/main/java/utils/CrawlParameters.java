package utils;

import org.jsoup.Jsoup;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * class to hold parameters for webcrawler
 */
public class CrawlParameters {
    public final String url;
    public final Integer depth;
    public final Locale language;


    private CrawlParameters(String url, Integer depth, String language) throws IllegalArgumentException, MalformedURLException {
        this.url = url;
        this.depth = depth;
        this.language = new Locale(language);
    }

    @Override
    public String toString() {
        return "Args{" +
                "url=" + url +
                ", depth=" + depth +
                ", language=" + language +
                '}';
    }

    /**
     * converts {String} args, checks them & creates a list of {CrawlParameters}
     *
     * @param args
     * @return List<CrawlParameters>
     * @throws IllegalArgumentException
     * @throws MalformedURLException
     */
    public static List<CrawlParameters> convert(String[] args) throws IllegalArgumentException, MalformedURLException {
        if (args.length != 3)
            throw new IllegalArgumentException("not enough arguments.");

        // validate
        String[] links = args[0].split(";");
        validateLinks(links);

        if (!isPositiveNumeric(args[1]))
            throw new IllegalArgumentException("not a valid depth. depth must be numeric and greater or equal to zero.");

        if (!isValidLanguage(args[2]))
            throw new IllegalArgumentException("not a valid language. language must be a valid locale string.");


        return convertLinksToListOfCrawlParameters(args, links);
    }


    private static List<CrawlParameters> convertLinksToListOfCrawlParameters(String[] args, String[] links) throws MalformedURLException {
        List<CrawlParameters> crawlParametersList = new ArrayList<>();
        for (int i = 0; i < links.length; i++) {
            crawlParametersList.add(new CrawlParameters(links[i], Integer.parseInt(args[1]), args[2]));

        }
        return crawlParametersList;
    }

    private static void validateLinks(String[] links) throws MalformedURLException {
        for (int i = 0; i < links.length; i++) {
            if (isLinkBroken(links[0])) throw new MalformedURLException("not a valid url : " + links[0]);
            System.out.println(links[i]);
        }
    }

    public static boolean isPositiveNumeric(String value) {
        try {
            return Integer.parseInt(value) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidLanguage(String languageString) {
        try {
            Locale locale = new Locale(languageString);
            return locale.getISO3Language() != null && locale.getISO3Country() != null;
        } catch (MissingResourceException | NullPointerException e) {
            return false;
        }
    }

    public static boolean isLinkBroken(String link) {
        try {
            Jsoup.connect(link);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

}
