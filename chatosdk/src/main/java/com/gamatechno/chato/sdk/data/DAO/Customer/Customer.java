package com.gamatechno.chato.sdk.data.DAO.Customer;

public class Customer {
    int customer_id;
    String customer_code, customer_app_id, customer_secret, customer_name_register, customer_company, customer_email, customer_phone, customer_address, customer_website, customer_count_user, customer_logo, customer_logo_url, insert_timestamp, is_active, is_deleted, auth_type;

    public Customer(String customer_app_id, String customer_secret) {
        this.customer_app_id = customer_app_id;
        this.customer_secret = customer_secret;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public String getCustomer_code() {
        return customer_code;
    }

    public String getCustomer_app_id() {
        return customer_app_id;
    }

    public String getCustomer_secret() {
        return customer_secret;
    }

    public String getCustomer_name_register() {
        return customer_name_register;
    }

    public String getCustomer_company() {
        return customer_company;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public String getCustomer_website() {
        return customer_website;
    }

    public String getCustomer_count_user() {
        return customer_count_user;
    }

    public String getCustomer_logo() {
        return customer_logo;
    }

    public String getCustomer_logo_url() {
        return customer_logo_url;
    }

    public String getInsert_timestamp() {
        return insert_timestamp;
    }

    public String getIs_active() {
        return is_active;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public String getAuth_type() {
        if(auth_type == null)
            return "MANUAL";
        return auth_type;
    }

    public static String auth_type_manual = "MANUAL";
    public static String aut_type_ldap = "LDAP";
}
