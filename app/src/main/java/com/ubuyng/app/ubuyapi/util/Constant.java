package com.ubuyng.app.ubuyapi.util;
/**
 * This is a v1 project for UBUYNG
 * And should not be reproduced or used without written consent from the author
 * Copyright 2019
 */
import com.ubuyng.app.BuildConfig;
import com.ubuyng.app.ubuyapi.Models.ItemSubcat;

import java.io.Serializable;
import java.util.ArrayList;

public class Constant implements Serializable {


    private static final long serialVersionUID = 1L;


    public static String SERVER_URL =  BuildConfig.server_url;


    //IMAGES
    public static final String IMAGE_PATH = SERVER_URL + "/uploads/images";

//    CONFIG API

    /**
     *
     *  CUSTOMER API STARTS HERE
     *
     */

//   CUSTOMER AUTH API
    public static final String REGISTER_URL = SERVER_URL + "auth/user_register?first_name=";

    public static final String LOGIN_URL = SERVER_URL + "auth/login?email=";

    public static final String PASSWORD_URL = SERVER_URL + "auth/forgetpass?email=";

    public static final String PROFILE_URL = SERVER_URL + "auth/user_profile?id=";

    public static final String REG_DETAILS_URL = SERVER_URL + "user_reg_details.php?email=";

    public static final String GOOGLE_AUTH_URL = SERVER_URL + "google_auth_api.php?email=";

    public static final String GOOGLE_AUTH_UPDATE_URL = SERVER_URL + "google_auth_update_api.php?email=";

    public static final String FORGOT_PASSWORD_URL = SERVER_URL + "user_forgot_pass_api.php?email=";

    //   HOME FEEDS
    public static final String HOME_FEEDS = "customers/home_feeds";

    //   CUSTOMER PROJECTS LIST
    public static final String CUSTOMER_PROJECTS = SERVER_URL + "customers/my_projects?user_id=";
    public static final String SEARCH_SUGGEST = SERVER_URL + "general/sudgest_cat?cat_id=";
    public static final String SEARCH_CAT = SERVER_URL + "general/search_cat";

    //   general dependencies
    public static final String SEARCH_SUBCAT = SERVER_URL + "general/fetch_subcategories?phrase=";
    public static final String SINGLESUBCAT = SERVER_URL + "general/subcategories/";
    public static final String QUESTIONCHOICE = SERVER_URL + "general/questions/";
    public static final String ALL_SUBCAT = SERVER_URL + "general/subcategories";
    public static final String RECOM_SEARCH = SERVER_URL + "general/search/subcategories";

//      CHAT SECTION
        public static final String SINGLE_CHAT = SERVER_URL + "inbox/singlechat?user_id=";
        public static final String SINGLE_PROJECT = SERVER_URL + "customers/bids?project_id=";
        public static final String STORE_MESSAGE = SERVER_URL + "inbox/project?user_id=";

    //    categories
    public static final String ALL_CATEGORIES = SERVER_URL + "customers/categories";

    //    tracker api
    public static final String PROJECT_TIMELINE = SERVER_URL + "project/timeline?project_id=";

    //    single bid api
    public static final String SINGLE_BID = SERVER_URL + "project/single_bid?bid_id=";

//    single bid profile api
    public static final String SINGLE_PROFILE = SERVER_URL + "profile/pro?pro_id=";
    public static final String SINGLE_PROFILE_ABOUT = SERVER_URL + "profile/about_me?pro_id=";
    public static final String SINGLE_PROFILE_PORTFOLIO = SERVER_URL + "profile/portfolio?pro_id=";
    public static final String SINGLE_PROFILE_PACKAGES = SERVER_URL + "profile/packages?pro_id=";
    public static final String SINGLE_PROFILE_FAQ = SERVER_URL + "profile/faq?pro_id=";


    public static final String CONTACT_US_URL = SERVER_URL + "api_contact.php?name=";


    public static final String ARRAY_NAME = "UBUYAPI_V2";
    public static final String ARRAY_NAME_V1 = "UBUYAPI_V1";


    public static final String PAGE_ID = "page_id";

    //todo@:: SUBCATEGORIES API
    public static final String SUB_ID = "id";
    public static final String SUB_REC_ID = "subcategory_id";
    public static final String SUB_TITLE = "name";
    public static final String SUB_SEC = "secondary_name";
    public static final String SUB_CAT_ID = "category_id";
    public static final String SUB_PIC = "image";
    public static final String SUB_ICON = "icon";
    public static final String SUB_DESCRIPTION = "description";
    public static final String SUB_TYPE = "payment_type";

    //todo@:: SLIDER API
    public static final String SLIDE_ID = "id";
    public static final String SLIDE_NAME = "name";
    public static final String SLIDE_PIC = "image";
    public static final String SLIDE_SOURCE_ID = "source_id";
    public static final String SLIDE_TYPE = "type";

    //todo@:: CATEGORIES API
    public static final String CAT_ID = "id";
    public static final String CAT_NAME = "name";
    public static final String CAT_PIC = "image";
    public static final String CAT_DES = "description";
    public static final String CAT_COLOR ="color";


    //todo@:: QUESTIONS API
    public static final String QUES_ID = "id";
    public static final String QUES_SUB_ID = "sub_category_id";
    public static final String QUES_TEXT = "text";
    public static final String QUES_REQUIRED = "required";
    public static final String QUES_TYPE = "type";

    //todo@:: CHOICES API
    public static final String CHOICE_ID = "id";
    public static final String CHOICE_QUES_ID = "question_id";
    public static final String CHOICE_TEXT = "text";
    public static final String CHOICE_DESC = "description";

    //todo@:: CHATTER API
    public static final String STATE_ID = "id";
    public static final String STATE_NAME = "name";
    public static final String STATE_CODE = "code";
    public static final String STATE_LIST = "state";



    //todo@:: CHATTER API
    public static final String CHATTER_HEADER = "bid_chats";
    public static final String CHATTER_ID = "chatter_id";
    public static final String CHATTER_MESSAGE = "chatter_message";
    public static final String CHATTER_IMAGE = "chatter_image";
    public static final String CHATTER_TIME = "chatter_time";
    public static final String CHATTER_SENDER = "chatter_sender";

    //todo@:: PROJECTS API
    public static final String PROJECT_HEADER = "project";
    public static final String PROJECTS_HEADER = "projects";
    public static final String PROJECT_ID = "project_id";
    public static final String PROJECT_SUB_ID = "sub_category_id";
    public static final String PROJECT_NAME = "sub_category_name";
    public static final String PROJECT_TITLE = "project_title";
    public static final String PROJECT_REF = "project_ref";
    public static final String PROJECT_MESSAGE = "brief";
    public static final String PROJECT_ADDRESS = "address";
    public static final String PROJECT_DATE = "created_at";
    public static final String PROJECT_STARTED_DATE = "started_at";
    public static final String PROJECT_DEADLINE_DATE = "deadline_at";
    public static final String PROJECT_USER_ID = "user_id";
    public static final String PROJECT_BID_COUNT = "bid_count";
    public static final String PROJECT_BID_STATUS = "bid_status";
    public static final String PROJECT_BID_HAS_CHAT = "has_chat";
    public static final String PROJECT_PROGRESS = "progress";
    public static final String PROJECT_BUDGET = "budget";
    public static final String PROJECT_AMOUNT = "task_amount";
    public static final String PROJECT_VERSION_ = "p_version";
    public static final String PROJECT_PRO_NAME = "pro_name";
    public static final String PROJECT_PRO_IMAGE = "selected_pro_image";
    public static final String PROJECT_DEADLINE = "project_deadline";


    //todo@:: TIMELINE API
    public static final String TRACKER_ID = "id";
    public static final String TRACKER_PROJ_ID = "project_id";
    public static final String TRACKER_USER_ID = "user_id";
    public static final String TRACKER_PRO_ID = "pro_id";
    public static final String TRACKER_BID_ID = "bid_id";
    public static final String TRACKER_TYPE = "track_type";
    public static final String TRACKER_PRO = "pro_name";
    public static final String TRACKER_MESSAGE = "message";
    public static final String TRACKER_DATE = "created_at";

    //todo@:: DISPUTES API
    public static final String DISPUTE_ID = "id";
    public static final String DISPUTE_CAT = "dispute_cat";
    public static final String DISPUTE_TASK = "dispute_task";
    public static final String DISPUTE_DES = "dispute_des";
    public static final String DISPUTE_REF = "dispute_ref";
    public static final String DISPUTE_DATE = "dispute_date";
    public static final String DISPUTE_STATUS = "dispute_status";
    public static final String DISPUTE_OPEN = "open_disputes";


//    todo@:: PROJECT CALL HEADERS
    public static final String PROJECT_PENDING = "project_pending";
    public static final String V3_PROJECT_PENDING = "v3_project_pending";
    public static final String V3_PROJECT_INPROGRESS = "v3_project_inprogress";
    public static final String PROJECT_COMPLETED = "project_completed";
    public static final String PROJECT_EXPIRED = "project_expired";
    public static final String PREMIUM_PROS = "premium_pros";
    public static final String TRENDING_PROS = "trending_pros";

    //    todo@:: UPAY  HEADERS
    public static final String UPAY_HISTORY = "upay_history";
    public static final String UPAY_BALANCE = "upay_balance";


    //    deprecated commands
    public static final String PROJECT_CAT_IMAGE = "category_image";

    //todo@:: PROJECT BID LIST API
    public static final String BID_HEADER = "project_bids";
    public static final String BID_ID = "bid_id";
    public static final String BID_PRO_ID = "pro_id";
    public static final String BID_MESSAGE = "bid_message";
    public static final String BID_AMOUNT = "bid_amount";
    public static final String BIDDER_IMAGE = "profile_photo";
    public static final String PRO_NAME = "pro_name";
    public static final String CUS_ID = "cus_id";
    public static final String BID_TYPE = "bid_type";
    public static final String BID_MATERIAL_FEE = "material_fee";
    public static final String BID_SERVICE_FEE = "service_fee";

    public static final String BID_STATUS = "bid_status";
    public static final String BID_DATE = "created_at";
    public static final String BIDDER_IMAGE_1 = "bidder_1_image";
    public static final String BIDDER_IMAGE_2 = "bidder_2_image";
    public static final String BIDDER_IMAGE_3 = "bidder_3_image";

//    TODO@:: PROS PROFILE API HEADERS
    public static final String PROFILE_HEADER = "pro_profile";
    public static final String PROFILE_RATING = "rating";
    public static final String PROFILE_BADGES = "badges";
    public static final String PROFILE_SERVICES = "services";
    public static final String PROFILE_ABOUT = "about";
    public static final String BIDS_FEED = "bids_feed";
    public static final String CUS_PROJECTS = "cus_projects";

    //    TODO@:: PROS PROFILE API
    public static final String PRO_ID = "pro_id";
    public static final String PRO_ABOUT = "pro_about";
    public static final String PRO_RATING = "pro_rating";
    public static final String TASK_DONE = "task_done";
    public static final String PRO_SERVICE = "pro_service";
    public static final String TASK_STATUS = "task_status";
    public static final String PRO_CITY = "pro_city";
    public static final String PREMIUM_PRO = "premium_pro";
    public static final String PRO_IMAGE = "pro_image";
    public static final String PRO_JOINED = "pro_joined";
    public static final String USER_VERIFIED = "user_verified";
    public static final String ABOUT_PRO = "about_pro";

    //    TODO@:: PROS PROFILE BADGE API HEADERS
    public static final String BADGE_PRO_ID = "badge_id";
    public static final String BADGE_EMAIL = "badge_email";
    public static final String BADGE_PHONE = "badge_number";
    public static final String BADGE_ID = "badge_id";

    //    TODO@:: PROS PROFILE PORTFOLIO API HEADERS
    public static final String PORTFOLIO_HEADER = "pro_portfolio";
    public static final String PORTFOLIO_ID = "portfolio_id";
    public static final String PORTFOLIO_title = "portfolio_title";
    public static final String PORTFOLIO_file = "portfolio_file";
    public static final String PORTFOLIO_likes = "portfolio_likes";
    public static final String PORTFOLIO_comments = "portfolio_comments";

    //    TODO@:: PROS PROFILE SERVICES API HEADERS
    public static final String SERVICE_HEADER = "pro_services";
    public static final String SERVICE_ID = "service_id";
    public static final String SERVICE_CATEGORY = "service_category_id";
    public static final String SERVICE_NAME = "service_name";
    public static final String SERVICE_PROJECTS = "service_projects";
    public static final String SERVICE_VERIFIED = "service_verified";
    public static final String SERVICE_IMAGE = "service_image";

    //    TODO@:: PROS PROFILE RATING API HEADERS
    public static final String RATING_ID = "rating_id";
    public static final String RATINGS = "rating";
    public static final String RATINGS_COMMENT = "rating_comment";
    public static final String RATINGS_TITLE = "rating_title";
    public static final String RATINGS_CUS_ID= "cus_id";
    public static final String RATINGS_PROJECT_NAME = "project_name";
    public static final String RATINGS_TYPE = "rate_type";
    public static final String RATINGS_CUS_NAME = "cus_name";
    public static final String RATINGS_IMAGE = "cus_image";
    public static final String RATINGS_DATE = "rate_date";

    //    NOTIFICATIONS
    public static final String NOTIFY_MSG = "notify_msg";
    public static final String NOTIFY_TYPE = "notify_type";
    public static final String NOTIFY_URL = "notify_url";

//   SETTINGS

    public static final String APP_NAME = "app_name";
    public static final String APP_IMAGE = "app_logo";
    public static final String APP_VERSION = "app_version";
    public static final String APP_AUTHOR = "app_author";
    public static final String APP_CONTACT = "app_contact";
    public static final String APP_EMAIL = "app_email";
    public static final String APP_WEBSITE = "app_website";
    public static final String APP_DESC = "app_description";
    public static final String APP_PRIVACY_POLICY = "app_privacy_policy";

    public static final String CHOICES_ARRAY_NAME = "choices";
    public static final String GALLERY_ARRAY_NAME = "galley_image";
    public static final String GALLERY_IMAGE_NAME = "image_name";

    //    USER AUTH JSONS
    public static final String USER_ID = "user_id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String USER_PHONE = "number";
    public static final String USER_EMAIL = "email";
    public static final String USER_TYPE = "user_role";
    public static final String USER_ALL_PROJECT= "user_project";
    public static final String USER_PHONE_VERIFY = "number_verify_code";
    public static final String USER_EMAIL_VERIFY = "email_verify_code";
    public static final String GOOGLE_NAME = "name";
    public static final String GOOGLE_AUTH = "google_auth";
    public static final String GOOGLE_EMAIL = "email";
    public static final String USER_PHOTO = "image";
    public static final String USER_ACCOUNT_NUMBER = "bank_transfer";
    public static final String USER_BANK_NAME = "bank_name";
    public static final String USER_ACCOUNT_NAME = "bank_name";



    public static final String HOME_SLIDER = "header_slide";
    public static final String HOME_RECOMMEND= "recommend";
    public static final String HOME_CAT= "category";
    public static final String HOME_BUSINESS = "business";
    public static final String HOME_PERSONAL ="personal";
    public static final String HOME_HOME ="home";
    public static final String HOME_PRO ="top_pros";


//    PROJECT FILE
    public static final String FILE_ID ="file_id";
    public static final String FILE_URL ="file_url";
    public static final String FILE_IS_IMAGE ="is_image";
    public static final String FILE_TYPE ="file_type";
    public static final String FILE_NAME ="file_name";
    public static final String FILE_DATE ="file_date";
//    SKILLS DATA
    public static final String SKILL_HEADER ="project_skill";
    public static final String SKILL_ID ="id";
    public static final String SKILL_TITLE ="skill_title";
    public static final String PROJECT_SKILL_1 ="project_skill_1";
    public static final String PROJECT_SKILL_2 ="project_skill_2";
    public static final String PROJECT_SKILL_3 ="project_skill_3";
    public static final String PROJECT_SKILL_4 ="project_skill_4";

//    UPAY
    public static final String UPAY_ID ="upay_id";
    public static final String UPAY_PROJECT_NAME ="upay_project_name";
    public static final String UPAY_PRO_NAME ="upay_pro_name";
    public static final String UPAY_PROJECT_ID ="upay_project_id";
    public static final String UPAY_AMOUNT ="upay_amount";
    public static final String UPAY_DATE ="upay_date";
    public static final String UPAY_BALANCE_AMOUNT ="upay_balance_total";



    public static int GET_SUCCESS_MSG;
    public static final String MSG = "msg";
    public static final String PROP_ID = "prop_id";
    public static final String PROP_TITLE = "prop_title";
    public static final String SUCCESS = "success";


//    CONFIG
    public static final String TEXT_REF = "tex_ref";
    public static final String TRANSACT_AMOUNT = "transact_amount";
    public static final String TRANSACT_PERCENT = "transact_percent";
    public static final String TRANSACT_FEE = "transact_fee";
    public static final String TRANSACT_TOTAL = "transact_total";
    public static final String TRANSACT_DURATION = "transact_duration";
    public static final String TRANSACT_FLUTTER = "transact_flutter_total";
    public static final String UPAY_TYPE = "upay_type";
    public static final String SIMPLE_ID = "id";
    public static final String SIMPLE_VERSION_BREAKER = "version_";
    public static final String SIMPLE_DATE = "created_at";
    public static final String SIMPLE_STATUS = "status";



    public static ArrayList<ItemSubcat> mList=new ArrayList<>();


}
