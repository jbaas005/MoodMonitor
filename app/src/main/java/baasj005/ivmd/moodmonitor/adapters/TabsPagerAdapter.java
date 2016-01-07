package baasj005.ivmd.moodmonitor.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesse on 6-1-2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitleList = new ArrayList<>();

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    /**
     * Returns fragment at position
     * @param position position of fragment to retrieve
     * @return fragment at parameter position
     */
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    /**
     *
     * @return total amount of fragments
     */
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * Add fragment and title to fragment list
     * @param fragment to add to fragment list
     * @param title to add to title list
     */
    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    /**
     * Returns fragment title at position
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}
