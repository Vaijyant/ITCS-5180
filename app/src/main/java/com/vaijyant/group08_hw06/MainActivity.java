package com.vaijyant.group08_hw06;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.vaijyant.group08_hw06.Fragments.AddInstructorFragment;
import com.vaijyant.group08_hw06.Fragments.CourseManagerFragment;
import com.vaijyant.group08_hw06.Fragments.CreateCourseFragment;
import com.vaijyant.group08_hw06.Fragments.InstructorListFragment;
import com.vaijyant.group08_hw06.Fragments.LoginFragment;
import com.vaijyant.group08_hw06.Fragments.RegisterFragment;
import com.vaijyant.group08_hw06.Models.Course;
import com.vaijyant.group08_hw06.Models.Instructor;
import com.vaijyant.group08_hw06.Models.User;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity
        implements
        LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        CourseManagerFragment.OnFragmentInteractionListener,
        AddInstructorFragment.OnFragmentInteractionListener,
        CreateCourseFragment.OnFragmentInteractionListener,
        InstructorListFragment.OnFragmentInteractionListener {

    Realm realm;
    public static boolean LOGGED_IN = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Realm
        Realm.init(this);

        // Get a Realm instance for this thread
        realm = Realm.getDefaultInstance();

        getFragmentManager().beginTransaction()
                .add(R.id.container, new LoginFragment(), "login")
                .commit();
    }

    /***********************************************************************************************
     * Menu
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_fa; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.actionHome:
                if (LOGGED_IN) {
                    setTitle("Course Manager");
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new CourseManagerFragment(), "home")
                            .commit();
                } else {
                    Toast.makeText(this, "Please login before proceeding.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.actionInstructors:
                if (LOGGED_IN) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new InstructorListFragment(), "instructor_list")
                            .commit();
                } else {
                    Toast.makeText(this, "Please login before proceeding.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.actionAddInstructors:
                if (LOGGED_IN) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new AddInstructorFragment(), "add_instructor")
                            .commit();
                } else {
                    Toast.makeText(this, "Please login before proceeding.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.actionLogout:
                if (!LOGGED_IN) {
                    Toast.makeText(this, "You have not logged in yet.", Toast.LENGTH_SHORT).show();
                } else {

                    LOGGED_IN = false;
                    setTitle("Course Manager");
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new LoginFragment(), "login")
                            .commit();
                }
                break;

            case R.id.actionExit:
                LOGGED_IN = false;
                finishAffinity();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /***********************************************************************************************
     * Login Fragment Methods
     * */

    @Override
    public void gotoRegisterFragment() {
        setTitle("Register");
        LOGGED_IN = true;
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new RegisterFragment(), "register")
                .commit();
    }

    @Override
    public void gotoCourseManager() {
        setTitle("Course Manager");
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new CourseManagerFragment(), "course_manager")
                .commit();


    }

    /***********************************************************************************************
     * Register Fragment Methods
     * */
    @Override
    public void register(final User user) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    realm.copyToRealm(user);
                    LOGGED_IN = true;
                    Toast.makeText(MainActivity.this, "Logged in as " + user.getUserName() + ".", Toast.LENGTH_SHORT).show();
                    gotoCourseManager();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "User exists. Please select another username.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /***********************************************************************************************
     * Course Manager Methods
     * */

    @Override
    public void gotoCreateCourseFragment() {
        setTitle("Create Course");
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new CreateCourseFragment(), "create_course")
                .commit();
    }

    /***********************************************************************************************
     * Add Instructor Methods
     * */
    @Override
    public void addInstructor(final Instructor instructor) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    realm.copyToRealm(instructor);
                    Toast.makeText(MainActivity.this, "Instructor added.", Toast.LENGTH_SHORT).show();
                    gotoCourseManager();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Instructor with same email exists." +
                            "Contact support.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /***********************************************************************************************
     * Create Course Methods
     * */
    @Override
    public void addCourse(final Course course) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    realm.copyToRealm(course);
                    Toast.makeText(MainActivity.this, "Course added.", Toast.LENGTH_SHORT).show();
                    gotoCourseManager();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Course with same title exists. " +
                            "Please select a different title.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /***********************************************************************************************
     * Instructor List Fragment Methods
     * */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
