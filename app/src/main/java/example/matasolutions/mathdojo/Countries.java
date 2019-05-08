package example.matasolutions.mathdojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Countries {

    public ArrayList<String> countryNames;

    public Countries(){

        countryNames = new ArrayList<>();
        getListOfCountries(Locale.ENGLISH);



    }


    public void getListOfCountries(Locale locale){

        String [] locales = Locale.getISOCountries();

        for(String countryCode : locales){

            Locale obj = new Locale("",countryCode);
            countryNames.add(obj.getDisplayCountry(locale));


        }


        Collections.sort(countryNames, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending

                String p1 = lhs;
                String p2 = rhs;


                return p1.compareTo(p2);}
        });

        countryNames.add(0,"Global");



    }

}
