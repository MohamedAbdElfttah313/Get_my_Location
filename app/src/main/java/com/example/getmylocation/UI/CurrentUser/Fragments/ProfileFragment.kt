package com.example.getmylocation.UI.CurrentUser.Fragments

import android.graphics.Color

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.getmylocation.Data.Remote.FirebaseGlobals
import com.example.getmylocation.UI.CurrentUser.*
import com.example.getmylocation.UI.CurrentUser.Fragments.ProfileInnerFragments.ProfileActivitiesFragment
import com.example.getmylocation.UI.CurrentUser.Fragments.ProfileInnerFragments.ProfileFriendsListFragment
import com.example.getmylocation.UI.CurrentUser.Fragments.ProfileInnerFragments.ProfileHealthStatusFragment
import com.example.getmylocation.UI.Dialogs.PickProfilePictureDialog
import com.example.getmylocation.UI.Dialogs.ShowPictureDialog
import com.example.getmylocation.R
import com.example.getmylocation.Domain.Models.NewUser
import com.example.getmylocation.UtilAndSystemServices.Downloader
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_current_user.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.profile_picture_bottom_sheet.*


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile , container  ,false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpCircleMenu()
        FirebaseGlobals.userInfo.let {
            setUpProfile(it)
        }

    }

    private fun setUpProfile(userInfo : NewUser?){
        Glide.with(this)
            .load(Uri.parse(userInfo!!.profilePicture))
            .into(ProfilePictureImageView)

        ProfileFullName.text = userInfo.run { "$firstName $lastName" }.toString()
    }




    private fun setUpCircleMenu(){
        ProfileCircleMenu.setMainMenu(Color.parseColor("#CDCDCD"),R.drawable.ic_baseline_menu_open_24,R.drawable.ic_baseline_close_24)
            .addSubMenu(Color.parseColor("#CDCDCD"),R.drawable.ic_baseline_image_24)
            .addSubMenu(Color.parseColor("#82B1FF"),R.drawable.ic_baseline_assignment_24)
            .addSubMenu(Color.parseColor("#CCFF90"),R.drawable.ic_baseline_medical_services_24)
            .addSubMenu(Color.parseColor("#84FFFF"),R.drawable.ic_baseline_people_alt_24)
            .addSubMenu(Color.parseColor("#B388FF"),R.drawable.ic_baseline_security_24)
            .setOnMenuSelectedListener {

                var selectedFragment = Fragment()
                GeneralProfileLayout.visibility = if (it==0) View.VISIBLE else View.GONE

                when(it){
                    0-> showBottomSheetDialog()

                    1-> selectedFragment = ProfileActivitiesFragment()

                    2-> selectedFragment = ProfileHealthStatusFragment()

                    3-> selectedFragment = ProfileFriendsListFragment()

                }
                this.childFragmentManager.beginTransaction().replace(R.id.SelectionFragment,selectedFragment).commit()
            }
    }

    private fun showBottomSheetDialog(){

        val bottomSheetDialog = BottomSheetDialog(requireContext()).apply {
            setContentView(R.layout.profile_picture_bottom_sheet)
        }

        bottomSheetDialog.apply {
            UploadNewPicView.setOnClickListener {
                dismiss()
                val pickNewPicDialog = PickProfilePictureDialog { pic2 ->
                    Glide.with(this@ProfileFragment).load(Uri.parse(pic2)).into(this@ProfileFragment.ProfilePictureImageView)
                }
                pickNewPicDialog.show(childFragmentManager , "NewPictureDialog")
            }

            ViewProfilePicView.setOnClickListener {
                ShowPictureDialog().show(childFragmentManager , "ShowDialog")
            }

            DownloadProfilePictView.setOnClickListener {
                FirebaseGlobals.userInfo.let { userInfo->
                    val uri = Uri.parse(userInfo!!.profilePicture)
                    Downloader().downloadFromUri(requireContext() , uri , System.currentTimeMillis().toString() ,
                        "Downloading From ${userInfo.nameForSearch}")
                    this.dismiss()
                }

            }

            CancelBottomSheetProfileView.setOnClickListener {
                this.dismiss()
            }
        }
        bottomSheetDialog.show()

    }
}