package org.rdc.capser.utilities;

public class ErrorForm {
    public static String errorForm(String message, String goBackAddress) {
        return "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"capsStyle.css\"></head><body><form><div>" + message + "</div></form><form action=\"" + goBackAddress + "\">\n" +
                "<div><button>Go to homepage</button></div>" +
                "</form></body></html>";
    }

    public static String successForm(String message) {
        return "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"capsStyle.css\"></head><body><form><div>" + message + "</div></form><form action=\"index.html\">\n" +
                "<div><button>Go to homepage</button></div>" +
                "</form></body></html>";
    }
}
