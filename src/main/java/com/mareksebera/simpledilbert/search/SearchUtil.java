package com.mareksebera.simpledilbert.search;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

final class SearchUtil {

    private SearchUtil() {
    }

    private static final Pattern url_match_pattern = Pattern
            .compile("var asPhrases(.*);$");

    public static List<String> extractUrls(HttpResponse response) {
        List<String> found = new ArrayList<String>();
        try {
            Scanner scan;
            Header contentEncoding = response
                    .getFirstHeader("Content-Encoding");
            if (contentEncoding != null
                    && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                scan = new Scanner(new GZIPInputStream(response.getEntity()
                        .getContent()));
            } else {
                scan = new Scanner(response.getEntity().getContent());
            }

            String match;
            while ((match = scan.findWithinHorizon(url_match_pattern, 0)) != null) {
                found.add(match.replace("/dyn/str_strip",
                        "http://www.dilbert.com/dyn/str_strip"));
            }
            scan.close();
            response.getEntity().consumeContent();
        } catch (Throwable t) {
            Log.e("FindUrls", "Error Occurred", t);
        }
        return found;
    }
}