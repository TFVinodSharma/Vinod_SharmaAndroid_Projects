/*
 * Copyright (C) 2017 Orange Tree technology Private Limited

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Orange Tree Technology private Limited
 * @developer  Rajiv Ranjan Singh
 *  Email : rajiv.ar73@gmail.com
 *
 * used to keep all utility methods which we used in the app
 */



package shifoo.com.app.modules.utility;

/**
 * Created by sony on 30-03-2015.
 */
public class AppValidationChecker {

    /**
     * use to validate email and password for sign in
     * @param email
     * @param password
     * @param iValidationResult
     */
//    public static void validateSignInWebService(String email,String password, IValidationResult iValidationResult)
//    {
//        if (TextUtils.isEmpty(email))
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_EMAIL_EMPTY, R.string.toast_enter_email);
//        }
//        else if (TextUtils.isEmpty(password))
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_PASSWORD_EMPTY, R.string.toast_enter_password);
//        }
//        else if (password.length() < 3 || password.length() > 20)
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_PASSWORD_LENGTH, R.string.toast_password_length);
//        }
//        else
//        {
//            iValidationResult.onValidationSuccess();
//        }
//    }



//
//    public static void validateSignUpWebService(String displayname,String mobileno,String firstName ,String password,String dob,String email,String address,IValidationResult iValidationResult)
//    {
//        if (TextUtils.isEmpty(displayname))
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_DISPLAY_NAME_EMPTY, R.string.toast_enter_display_name);
//        }
//        else if (TextUtils.isEmpty(mobileno))
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_MOBILENO_EMPTY, R.string.toast_enter_mobileno);
//
//        }
//
//        else if (mobileno.trim().length()<10)
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_MOBILENO_LENGTH, R.string.toast_enter_mobileno_length);
//
//        }
//
//        else if (TextUtils.isEmpty(firstName))
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_FIRSTNAME_EMPTY, R.string.toast_enter_firstname);
//
//        }
//        else if (TextUtils.isEmpty(password))
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_PASSWORD_EMPTY, R.string.toast_enter_password);
//
//        }
//
//        else if (TextUtils.isEmpty(dob))
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_DOB_EMPTY, R.string.toast_enter_dob);
//
//        }
//
//        else if (TextUtils.isEmpty(email))
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_EMAIL_EMPTY, R.string.toast_enter_email);
//
//        }
//
//        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_EMAIL_INVALID, R.string.toast_enter_valid_email);
//        }
//
//        else if (TextUtils.isEmpty(address))
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_ADDRESS_EMPTY, R.string.toast_enter_address);
//
//        }
//
//        else if (password.length() < 3 || password.length() > 20)
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_PASSWORD_LENGTH, R.string.toast_password_length);
//        }
//
//        else
//        {
//            iValidationResult.onValidationSuccess();
//        }
//    }
//
//    /**
//     * used to validate fields for forgot password web service
//     * @param email
//     * @param iValidationResult
//     */
//    public static void validateForgotPasswordWebService(String email,IValidationResult iValidationResult)
//    {
//        if (TextUtils.isEmpty(email))
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_EMAIL_EMPTY,R.string.toast_enter_email);
//        }
//        else
//        {
//            iValidationResult.onValidationSuccess();
//        }
//    }
//
//
//
//
//    public static void validateMeetingWebService(String date,String time,String attendees, IValidationResult iValidationResult)
//    {
//        if (TextUtils.isEmpty(date))
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_DATE_EMPTY, R.string.toast_enter_date);
//        }
//        else if (TextUtils.isEmpty(time))
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_TIME_EMPTY, R.string.toast_enter_time);
//        }
//
//
//        else if (attendees.length() < 1 )
//        {
//            iValidationResult.onValidationError(ErrorConstant.ERROR_TYPE_ATTENDEES_EMPTY, R.string.toast_enter_attendees);
//        }
//
//        else
//        {
//            iValidationResult.onValidationSuccess();
//        }
//    }

}
