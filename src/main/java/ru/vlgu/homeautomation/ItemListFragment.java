package ru.vlgu.homeautomation;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ItemListFragment extends ListFragment {

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (position) {
            case 0: {
                SystemStateFragment fragment = new SystemStateFragment();
                fragmentTransaction.replace(R.id.item_detail_container, fragment);
                break;
            }
            case 1: {
                LightFragment fragment = new LightFragment();
                fragmentTransaction.replace(R.id.item_detail_container, fragment);
                break;
            }
            case 2: {
                ClimateFragment fragment = new ClimateFragment();
                fragmentTransaction.replace(R.id.item_detail_container, fragment);
                break;
            }
            case 3: {
                CameraFragment fragment = new CameraFragment();
                fragmentTransaction.replace(R.id.item_detail_container, fragment);
                break;
            }
            case 4: {
                OtherFragment fragment = new OtherFragment();
                fragmentTransaction.replace(R.id.item_detail_container, fragment);
                break;
            }
            case 5: {
                SettingsFragment fragment = new SettingsFragment();
                fragmentTransaction.replace(R.id.item_detail_container, fragment);
                break;
            }
            case 6: {
                AboutFragment fragment = new AboutFragment();
                fragmentTransaction.replace(R.id.item_detail_container, fragment);
                break;
            }
        }

        fragmentTransaction.commit();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] menu_items = getActivity().getResources().getStringArray(R.array.menu_items);
        int layout = (Build.VERSION.SDK_INT >= 11) ? android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(getActivity(), layout, menu_items);
        setListAdapter(adapter);
    }
}
