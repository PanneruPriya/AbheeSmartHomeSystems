package com.abhee.abheesmarthomesystems;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Admin on 15-11-2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    int i ;
    public ViewPagerAdapter(FragmentManager fm,int i) {
        super(fm);
        this.i=i;


    }



    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:


                TicketstatusquotationActivity tab1=new TicketstatusquotationActivity().newInstance();


                return tab1;

            case 1:
                TicketStatusActivity tab2= new TicketStatusActivity().newInstance();

                return tab2;
        }
        return null;

    }

    @Override
    public int getCount() {

        return i;
    }

}
