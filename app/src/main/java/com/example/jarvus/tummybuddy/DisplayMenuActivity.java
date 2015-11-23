package com.example.jarvus.tummybuddy;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DisplayMenuActivity extends ListActivity {
    public static final int MENU_ERROR = -1;
    public static final int MENU_SIXTY_FOUR = 0;
    public static final int MENU_CANYON_VISTA = 1;
    public static final int MENU_CAFE_VENTANAS = 2;
    public static final int MENU_CLUB_MED = 3;
    public static final int MENU_FOODWORX = 4;
    public static final int MENU_GOODYS = 5;
    public static final int MENU_PINES = 6;
    public static final int MENU_ROOTS = 7;
    public static final int MENU_BISTRO = 8;

   // public static final String hour_type1 = String.format("Mon - Thurs: %s am - %s pm \n Fri - Sun: %s am - % pm");
    public static final String hour_type1 = "Mon - Thurs: %s \n Fri - Sun: %s ";
    public static final String hour_type2 = "Mon - Thurs: %s \n Fri: %s | Sat - Sun: %s ";
    public static final String hour_type3 = "Mon - Fri: %s \n Sat - Sun: %s";

    private TextView data_text;
    private String url_data;
    private int time = 0;
    private int hour = 0;
    private boolean is_Special = false;
    private ArrayList<Object> items;
    private ItemAdapter iAdapt;
    private  ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        listView = getListView();
        items = new ArrayList<>();
        iAdapt = new ItemAdapter(this, items);
        listView.setAdapter(iAdapt);
        // Set variable as menu error by default
        int menu = getIntent().getIntExtra(MainActivity.EXTRA_DINING_HALL, MENU_ERROR);
        String menuName = getString(R.string.error_loading);
        TextView textView = (TextView) findViewById(R.id.menu_name);

        // Set hours of each dinning hall
        String dining_hours = getString(R.string.error_loading);
        TextView hour_text = (TextView) findViewById(R.id.hours);

        // Set the current date
        String currentDate = DateFormat.getDateInstance().format(new Date());
        TextView date_text = (TextView) findViewById(R.id.date);

        if(menu == MENU_SIXTY_FOUR) {
            menuName = getString(R.string.button_sixty_four);
            dining_hours = String.format(hour_type1, "10 am - 9 pm", "10 am - 8 pm");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=64";

             }
        else if (menu == MENU_CANYON_VISTA) {
            menuName = getString(R.string.button_canyon);
            dining_hours = String.format(hour_type2, "7:30 am - 9 pm", "7:30 am - 8 pm", "10 am - 8 pm");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=24";
        }
        else if (menu == MENU_CAFE_VENTANAS) {
            menuName = getString(R.string.button_ventanas);
            dining_hours = String.format(hour_type2, "7:30 am - 9 pm", "7:30 am - 8 pm", "10 am - 8 pm");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=18";
        }
        else if (menu == MENU_CLUB_MED) {
            menuName = getString(R.string.button_med);
            dining_hours = String.format(hour_type3, "7:30 am - 2 pm", "Closed");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=15";
            is_Special = true;
        }
        else if (menu == MENU_FOODWORX) {
            menuName = getString(R.string.button_foodworx);
            dining_hours = String.format(hour_type2, "7:30 am - 10 pm", "7:30 am - 8 pm", "10 am - 8 pm");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=11";
        }
        else if (menu == MENU_GOODYS) {
            menuName = getString(R.string.button_goodys);
            dining_hours = String.format(hour_type3, "8 am - 10 pm", "11 am - 10 pm");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=06";
            is_Special = true;
        }
        else if (menu == MENU_PINES) {
            menuName = getString(R.string.button_pines);
            dining_hours = String.format(hour_type2, "7:30 am - 9 pm", "7:30 am - 8 pm", "10 am - 8 pm");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=01";
        }
        else if (menu == MENU_ROOTS) {
            menuName = getString(R.string.button_roots);
            dining_hours = String.format(hour_type3, "11 am - 8 pm", "Closed");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=32";
            is_Special = true;
        }
        else if (menu == MENU_BISTRO) {
            menuName = getString(R.string.button_bistro);
            dining_hours = String.format(hour_type3, "11 am - 9 pm", "Closed");
            url_data = "http://hdh.ucsd.edu/DiningMenus/default.aspx?i=27";
            is_Special = true;
        }

        textView.setText(menuName);
        hour_text.setText(dining_hours);
        date_text.setText(currentDate);

        // Parsing data when clicking the button
        Button btn1 = (Button) findViewById(R.id.button1);
        Button btn2 = (Button) findViewById(R.id.button2);
        Button btn3 = (Button) findViewById(R.id.button3);


        //data_text.setMovementMethod(new ScrollingMovementMethod());
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = 1;
                (new ParseURL()).execute(new String[]{url_data});
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = 2;
                (new ParseURL()).execute(new String[]{url_data});
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time = 3;
                (new ParseURL()).execute(new String[]{url_data});
            }
        });

        // Set the default menu
        if( time == 0 ) {
            new ParseURL().execute(new String[]{url_data});
        }
        // Set the text view as the activity layout
    }

    @Override
    protected void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);
        Object o = listView.getItemAtPosition(pos);
        Item it = (Item) o;

        Toast.makeText(this, it.getName() + " clicked.", Toast.LENGTH_SHORT).show();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("DiningHall");
        query.whereEqualTo("menu", it.getName());

        try {
            List<ParseObject> objects = query.find();
            if (objects != null && objects.size() > 0) {
                for (ParseObject dealsObject : objects) {
                    String uid = (String)dealsObject.get("distinct_id");
                    if(uid.matches("[-+]?\\d*\\.?\\d+")) {
                        it.setID(uid);
                    }
                }
            }
        } catch (ParseException e) {
            Log.d("distinct_id", "Error: " + e.getMessage());
        }

        if (it.hasID()) {
            try {
                String nutri_link = "http://hdh.ucsd.edu/DiningMenus/nutritionfacts.aspx?i=" + it.getID();
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(nutri_link));
                startActivity(myIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application can handle this request."
                        + " Please install a web browser", Toast.LENGTH_SHORT).show();
                //e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No Nutrition Facts Found", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed() {
        finish();
    }

    private class ParseURL extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings){

            // Get the time of day
            Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);

            StringBuffer buffer = null;
            StringBuffer breakFast = new StringBuffer();
            StringBuffer lunch = new StringBuffer();
            StringBuffer dinner = new StringBuffer();

            try{
                Document doc = Jsoup.connect(strings[0]).get();

                // Get title
                String title = doc.title();

                Elements menuList;
                Elements section;

                // Get body
                if(is_Special == true){
                    menuList = doc.select("table.menuContainer td.specialtyMenuList ul.SpecialtyItemList");
                    section = doc.select("table.menuContainer p.category");
                }else{
                    menuList = doc.select("table#MenuListing_tblDaily ul.itemList");
                    section = doc.select("table#MenuListing_tblDaily td.menuList p.category");
                }

                String tmp = null;
                int section_ctr = 0;
                int numOfTags = 0;

                for(int x=0; x<menuList.size(); x++){

                    if(menuList.size() == 2){
                        if(x == 0){
                            breakFast.append(section.get(0).text());
                            section.remove(0);
                            buffer = lunch;
                        }
                        else if(x == 1)
                            buffer = dinner;
                    }else if(menuList.size()==3){
                        if(x == 0)
                            buffer = breakFast;
                        else if(x == 1)
                            buffer = lunch;
                        else
                            buffer = dinner;
                    }else{
                        // Menu for breakfast, lunch and dinner is same
                        buffer = breakFast = lunch = dinner;
                    }

                    // Assign buffer ref to appripriate object among breakfast, lunch and dinner
                    int menu_ctr = 1;
                    numOfTags = menuList.get(x).getElementsByTag("p").size() +
                            menuList.get(x).getElementsByTag("li").size();

                    buffer.append("section:" + section.get(section_ctr).text());

                    tmp = section.get(++section_ctr).text();
                    // Iterate every menu for breakfast, lunch or dinner.
                    while (menu_ctr < numOfTags) {
                        if (tmp.equals(menuList.get(x).child(menu_ctr).text())) {

                            buffer.append("\nsection:" + section.get(section_ctr).text());
                            menu_ctr++;
                            if(! (section_ctr+1 == section.size()))
                                tmp = section.get(++section_ctr).text();
                        }
                        buffer.append("\n" + menuList.get(x).child(menu_ctr).text());
                        menu_ctr++;
                    }
                }

                // Set default menu
                if(time == 0) {
                    if(hour < 12)
                        buffer = breakFast;
                    else if(hour < 18)
                        buffer = lunch;
                    else
                     buffer = dinner;
                }

                // append appropriate menu based on the value of "time" which is set by
                // among breakfast, lunch or dinner button
                if(time == 1)
                    buffer = breakFast;
                else if(time == 2)
                    buffer = lunch;
                else if(time == 3)
                    buffer = dinner;

            }catch(Throwable t){
                t.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            if(!items.isEmpty())
                items.clear();
            String[] ls = s.split("\n");

            for(String str : ls) {
                String[] strSp = str.split(":");
                if(strSp[0].equals("section")) {
                    items.add(strSp[1]);
                } else {
                    strSp = strSp[0].split("\u00a0(\u00a0)+");
                    Item it = new Item(strSp[0]);

                    if(strSp.length > 1)
                        it.setPrice(strSp[1]);
                    items.add(it);
                }
            }
            iAdapt.notifyDataSetChanged();
        }
    }
}
