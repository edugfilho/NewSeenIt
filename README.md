NewSeenIt
=========

New Repo!

Test Test Test!
Ways to treat a fragment: 
From main activity
 Fragment currentFragment = getFragmentManager().findFragmentByTag("FRAGMENT");
    FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
    fragTransaction.detach(currentFragment);
    fragTransaction.attach(currentFragment);
    fragTransaction.commit();