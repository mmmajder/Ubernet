package com.example.ubernet.utils;

public class EmailContentUtils {
    public static String getVerificationContent() {
        String title = "Verify your email address\r\n";
        String body = "To complete your profile registration, you'll need to verify your email address.\r\n";
        String buttonText = "VERIFY\r\n";
        return originalTemplate(title, body, buttonText);
    }

    public static String getResetPasswordContent() {
        String title = "Set new password\r\n";
        String body = "Click the button below to set your new password.\r\n";
        String buttonText = "Set password\r\n";
        return originalTemplate(title, body, buttonText);
    }

    private static String originalTemplate(String title, String body, String buttonText) {
        String button = "";
        if (buttonText!="") {
            button =  "                                    <a href=\"[[URL]]\" target=\"_blank\" style=\"font-family: Arial, Helvetica, sans-serif; font-size: 16px; line-height: 1.0; font-weight: bold; color: #ffffff; text-transform: uppercase; text-decoration: none; border-radius: 30px; -webkit-border-radius: 30px; -moz-border-radius: 30px; display: block; padding: 12px 25px 12px 25px;\">\r\n"
                    + buttonText
                    + "                                            </a>\r\n";
        }

        String content = "<!-- Automatically generated by make_email v0.8.0. Do not edit this file. -->\r\n"
                + "<!-- Automatically generated by make_email v0.8.0. Do not edit this file. -->\r\n"
                + "<!-- Automatically generated by make_email v0.8.0. Do not edit this file. -->\r\n"
                + "<!-- Automatically generated by make_email v0.8.0. Do not edit this file. -->\r\n"
                + "<!-- Automatically generated by make_email v0.8.0. Do not edit this file. -->\r\n"
                + "<!-- Automatically generated by make_email v0.8.0. Do not edit this file. -->\r\n"
                + "<!-- Automatically generated by make_email v0.8.0. Do not edit this file. -->\r\n"
                + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n"
                + "\r\n"
                + "<head>\r\n"
                + "  <meta name=\"x-apple-disable-message-reformatting\" />\r\n"
                + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\r\n"
                + "  <link rel=\"icon\" href=\"https://growth.lyft.com.s3.amazonaws.com/lyft%20favicon.ico\" type=\"image/gif\" sizes=\"32x32\">\r\n"
                + "  <title>Lyft</title>\r\n"
                + "  <style type=\"text/css\">\r\n"
                + "    /*******************FONT STYLING LYFT*********************/\r\n"
                + "\r\n"
                + "    @import url(http://lyft-assets.s3.amazonaws.com/font/Lyft%20Pro/LyftPro-Bold.otf);\r\n"
                + "    @font-face {\r\n"
                + "      font-family: 'LyftPro-Bold';\r\n"
                + "      src: url(https://lyft-assets.s3.amazonaws.com/font/Lyft%20Pro/LyftPro-Bold.otf) format(\"opentype\");\r\n"
                + "      font-weight: bold;\r\n"
                + "      font-style: normal;\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    @import url(http://lyft-assets.s3.amazonaws.com/font/Lyft%20Pro/LyftPro-Regular.otf);\r\n"
                + "    @font-face {\r\n"
                + "      font-family: 'LyftPro-Regular';\r\n"
                + "      src: url(https://lyft-assets.s3.amazonaws.com/font/Lyft%20Pro/LyftPro-Regular.otf) format(\"opentype\");\r\n"
                + "      font-weight: normal;\r\n"
                + "      font-style: normal;\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    @import url(https://lyft-assets.s3.amazonaws.com/font/Lyft%20Pro/LyftPro-SemiBold.otf);\r\n"
                + "    @font-face {\r\n"
                + "      font-family: 'LyftPro-SemiBold';\r\n"
                + "      src: url(https://lyft-assets.s3.amazonaws.com/font/Lyft%20Pro/LyftPro-SemiBold.otf) format(\"opentype\");\r\n"
                + "      font-weight: normal;\r\n"
                + "      font-style: normal;\r\n"
                + "    }\r\n"
                + "\r\n"
                + "\r\n"
                + "    /************************* END FONT STYLING ************************************/\r\n"
                + "\r\n"
                + "    body {\r\n"
                + "      width: 100%;\r\n"
                + "      background-color: #FFFFFF;\r\n"
                + "      margin: 0;\r\n"
                + "      padding: 0;\r\n"
                + "      -webkit-font-smoothing: antialiased;\r\n"
                + "      font-family: 'Open Sans', Arial, sans-serif;\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    table {\r\n"
                + "      border-collapse: collapse;\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    img {\r\n"
                + "      border: 0;\r\n"
                + "      outline: none !important;\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    .hideDesktop {\r\n"
                + "      display: none;\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    /********* CTA Style - fixed padding *********/\r\n"
                + "\r\n"
                + "    .cta-shadow {\r\n"
                + "      padding: 14px 35px;\r\n"
                + "      -webkit-box-shadow: 0px 5px 0px rgba(0, 0, 0, 0.2);\r\n"
                + "      -moz-box-shadow: 0px 5px 0px rgba(0, 0, 0, 0.2);\r\n"
                + "      box-shadow: 0px 5px 0px rgba(0, 0, 0, 0.2);\r\n"
                + "      -moz-border-radius: 25px;\r\n"
                + "      -webkit-border-radius: 25px;\r\n"
                + "      font-size: 16px;\r\n"
                + "      font-weight: normal;\r\n"
                + "      letter-spacing: 0px;\r\n"
                + "      text-decoration: none;\r\n"
                + "      display: block;\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    body[yahoo] .hideDeviceDesktop {\r\n"
                + "      display: none;\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    @media only screen and (max-width: 640px) {\r\n"
                + "\r\n"
                + "      div[class=mobilecontent] {\r\n"
                + "        display: block !important;\r\n"
                + "        max-height: none !important;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      body[yahoo] .fullScreen {\r\n"
                + "        width: 100% !important;\r\n"
                + "        padding: 0px;\r\n"
                + "        height: auto;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      body[yahoo] .halfScreen {\r\n"
                + "        width: 50% !important;\r\n"
                + "        padding: 0px;\r\n"
                + "        height: auto;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      body[yahoo] .mobileView {\r\n"
                + "        width: 100% !important;\r\n"
                + "        padding: 0 4px;\r\n"
                + "        height: auto;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      body[yahoo] .center {\r\n"
                + "        text-align: center !important;\r\n"
                + "        height: auto;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      body[yahoo] .hideDevice {\r\n"
                + "        display: none;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      body[yahoo] .hideDevice640 {\r\n"
                + "        display: none;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      body[yahoo] .showDevice {\r\n"
                + "        display: table-cell !important;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      body[yahoo] .showDevice640 {\r\n"
                + "        display: table !important;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "\r\n"
                + "      body[yahoo] .googleCenter {\r\n"
                + "        margin: 0 auto;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      .mobile-LR-padding-reset {\r\n"
                + "        padding-left: 0 !important;\r\n"
                + "        padding-right: 0 !important;\r\n"
                + "      }\r\n"
                + "      .side-padding-mobile {\r\n"
                + "        padding-left: 40px;\r\n"
                + "        padding-right: 40px;\r\n"
                + "      }\r\n"
                + "      .RF-padding-mobile {\r\n"
                + "        padding-top: 0 !important;\r\n"
                + "        padding-bottom: 25px !important;\r\n"
                + "      }\r\n"
                + "      .wrapper {\r\n"
                + "        width: 100% !important;\r\n"
                + "      }\r\n"
                + "      .two-col-above {\r\n"
                + "        display: table-header-group;\r\n"
                + "      }\r\n"
                + "      .two-col-below {\r\n"
                + "        display: table-footer-group;\r\n"
                + "      }\r\n"
                + "      .hideDesktop {\r\n"
                + "        display: block !important;\r\n"
                + "      }\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    @media only screen and (max-width: 520px) {\r\n"
                + "      .mobileHeader {\r\n"
                + "        font-size: 50px !important;\r\n"
                + "      }\r\n"
                + "      .mobileBody {\r\n"
                + "        font-size: 16px !important;\r\n"
                + "      }\r\n"
                + "      .mobileSubheader {\r\n"
                + "        font-size: 30px !important;\r\n"
                + "      }\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    @media only screen and (max-width: 479px) {\r\n"
                + "\r\n"
                + "      body[yahoo] .fullScreen {\r\n"
                + "        width: 100% !important;\r\n"
                + "        padding: 0px;\r\n"
                + "        height: auto;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      body[yahoo] .mobileView {\r\n"
                + "        width: 100% !important;\r\n"
                + "        padding: 0 4px;\r\n"
                + "        height: auto;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      body[yahoo] .center {\r\n"
                + "        text-align: center !important;\r\n"
                + "        height: auto;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      body[yahoo] .hideDevice {\r\n"
                + "        display: none;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      body[yahoo] .hideDevice479 {\r\n"
                + "        display: none;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      body[yahoo] .showDevice {\r\n"
                + "        display: table-cell !important;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      body[yahoo] .showDevice479 {\r\n"
                + "        display: table !important;\r\n"
                + "      }\r\n"
                + "\r\n"
                + "      .mobile-LR-padding-reset {\r\n"
                + "        padding-left: 0 !important;\r\n"
                + "        padding-right: 0 !important;\r\n"
                + "      }\r\n"
                + "      .side-padding-mobile {\r\n"
                + "        padding-left: 40px;\r\n"
                + "        padding-right: 40px;\r\n"
                + "      }\r\n"
                + "      .RF-padding-mobile {\r\n"
                + "        padding-top: 0 !important;\r\n"
                + "        padding-bottom: 25px !important;\r\n"
                + "      }\r\n"
                + "      .wrapper {\r\n"
                + "        width: 100% !important;\r\n"
                + "      }\r\n"
                + "      .two-col-above {\r\n"
                + "        display: table-header-group;\r\n"
                + "      }\r\n"
                + "      .two-col-below {\r\n"
                + "        display: table-footer-group;\r\n"
                + "      }\r\n"
                + "      .mobileButton {\r\n"
                + "        width: 150px !important !\r\n"
                + "      }\r\n"
                + "\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    @media only screen and (max-width: 385px) {\r\n"
                + "      .mobileHeaderSmall {\r\n"
                + "        font-size: 18px !important;\r\n"
                + "        padding-right: none;\r\n"
                + "      }\r\n"
                + "      .mobileBodySmall {\r\n"
                + "        font-size: 14px !important;\r\n"
                + "        padding-right: none;\r\n"
                + "      }\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    /* Stops automatic email inks in iOS */\r\n"
                + "\r\n"
                + "    a[x-apple-data-detectors] {\r\n"
                + "\r\n"
                + "      color: inherit !important;\r\n"
                + "\r\n"
                + "      text-decoration: none !important;\r\n"
                + "\r\n"
                + "      font-size: inherit !important;\r\n"
                + "\r\n"
                + "      font-family: inherit !important;\r\n"
                + "\r\n"
                + "      font-weight: inherit !important;\r\n"
                + "\r\n"
                + "      line-height: inherit !important;\r\n"
                + "\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    a[href^=\"x-apple-data-detectors:\"] {\r\n"
                + "      color: inherit;\r\n"
                + "      text-decoration: inherit;\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    .footerLinks {\r\n"
                + "      text-decoration: none;\r\n"
                + "      color: #384049;\r\n"
                + "      font-family: 'LyftPro-Regular', 'Helvetica Neue', Helvetica, Arial, sans-serif;\r\n"
                + "      font-size: 12px;\r\n"
                + "      line-height: 18px;\r\n"
                + "      font-weight: normal;\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    /*******Some Clients do not support rounded borders (example: older versions of Outlook)**********/\r\n"
                + "\r\n"
                + "    .roundButton {\r\n"
                + "      border-radius: 5px;\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    /************************* Fixing Auto Styling for Gmail*********************/\r\n"
                + "\r\n"
                + "    .contact a {\r\n"
                + "      color: #88888f !important !;\r\n"
                + "      text-decoration: none;\r\n"
                + "    }\r\n"
                + "\r\n"
                + "    u+#body a {\r\n"
                + "      color: inherit;\r\n"
                + "      text-decoration: none;\r\n"
                + "      font-size: inherit;\r\n"
                + "      font-family: inherit;\r\n"
                + "      font-weight: inherit;\r\n"
                + "      line-height: inherit;\r\n"
                + "    }\r\n"
                + "  </style>\r\n"
                + "  <!-- Fall-back font for Outlook (Arial) -->\r\n"
                + "  <!--[if (gte mso 9)|(IE)]>\r\n"
                + "\r\n"
                + "    <style type=\"text/css\">\r\n"
                + "\r\n"
                + "    a, body, div, li, p, strong, td, span {font-family: Arial, Helvetica, sans-serif !important;}\r\n"
                + "    \r\n"
                + "    </style>\r\n"
                + "\r\n"
                + "  <![endif]-->\r\n"
                + "</head>\r\n"
                + "\r\n"
                + "<body leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\" yahoo=\"fix\" style=\"font-family: 'Open Sans', Arial, sans-serif;\" align=\"center\" id=\"body\">\r\n"
                + "  <custom type=\"content\" name=\"ampscript\">\r\n"
                + "    <!-- FULL PAGE WIDTH WRAPPER WITH TINT -->\r\n"
                + "    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\r\n"
                + "      <tr>\r\n"
                + "        <td align=\"center\" bgcolor=\"#f3f3f5\" valign=\"top\" width=\"100%\">\r\n"
                + "          <!--========= WHITE PAGE BODY CONTAINER/WRAPPER========-->\r\n"
                + "          <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"mobileView\" width=\"600\" style=\"\">\r\n"
                + "            <tr>\r\n"
                + "              <td align=\"center\" bgcolor=\"#FFFFFF\" style=\"padding:0px;\" width=\"100%\">\r\n"
                + "\r\n"
                + "                <!--================================SECTION 0==========================-->\r\n"
                + "                <table align=\"center\" bgcolor=\"\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"fullScreen\" style=\"width:100% !important;background-color:#625a9c;\" width=\"600\">\r\n"
                + "                  <tr>\r\n"
                + "                    <td bgcolor=\"#FFD6E5\" class=\"\" style=\"width:100% !important; padding: 0;background-color:#ffffff;\">\r\n"
                + "                      <!--========Paste your Content below=================-->\r\n"
                + "                      <table cellspacing=\"0\" cellpadding=\"0\" align=\"center\" border=\"0\" width=\"100%\" bgcolor=\"#F3F3F5\">\r\n"
                + "                        <tr>\r\n"
                + "                          <td class=\"divider\" align=\"center\" height=\"16px\" style=\"background-color: #F3F3F5;\">\r\n"
                + "                          </td>\r\n"
                + "                        </tr>\r\n"
                + "                      </table>\r\n"
                + "                      <table cellspacing=\"0\" cellpadding=\"0\" align=\"center\" border=\"0\" width=\"100%\" bgcolor=\"#F3F3F5\">\r\n"
                + "                        <tr>\r\n"
                + "                          <td align=\"center\" height=\"25px\" style=\"background-color: #FFFFFF;\">\r\n"
                + "                          </td>\r\n"
                + "                        </tr>\r\n"
                + "                      </table>\r\n"
                + "                      <!-- BEGIN LOGO -->\r\n"
                + "                      <table cellspacing=\"0\" cellpadding=\"0\" align=\"left\" border=\"0\" width=\"100%\" bgcolor=\"#ffffff\">\r\n"
                + "                        <tr>\r\n"
                + "                          <td valign=\"top\" align=\"left\" width=\"100%\" style=\"padding-left: 25px; padding-right: 25px;\">\r\n"
                + "                            \r\n"
                + "                            <div style=\"padding-left: 25px; height: auto;\"  />\r\n"
                + "                              \r\n"
                + "                              <span style=\"font-family: Gill Sans,Gill Sans MT,Calibri,sans-serif; font-size: 60px; color:#5da4b4\" > \r\n"
                + "                                Ubernet\r\n"
                + "                              </span>\r\n"
                + "\r\n"
                + "                              <img align=\"right\" style=\"max-width: 80px\" src=\"https://photoshop-kopona.com/uploads/posts/2019-06/1560711338_01-4.jpg\" alt=\"Logo\"> </img>\r\n"
                + "                            </div>\r\n"
                + "                            \r\n"
                + "                          </td>\r\n"
                + "                        </tr>\r\n"
                + "                      </table>\r\n"
                + "                      \r\n"
                + "                      <table cellspacing=\"0\" cellpadding=\"0\" align=\"center\" border=\"0\" width=\"100%\" bgcolor=\"#F3F3F5\">\r\n"
                + "                        <tr>\r\n"
                + "                          <td align=\"center\" height=\"25px\" style=\"background-color: #FFFFFF;\">\r\n"
                + "                          </td>\r\n"
                + "                        </tr>\r\n"
                + "                      </table>\r\n"
                + "                      <!-- END LOGO -->\r\n"
                + "\r\n"
                + "                      <!-- nothing -->\r\n"
                + "\r\n"
                + "                      <!--=======End your Content here=====================-->\r\n"
                + "                    </td>\r\n"
                + "                  </tr>\r\n"
                + "                </table>\r\n"
                + "                <!--=======END SECTION==========-->\r\n"
                + "\r\n"
                + "                <table align=\"center\" bgcolor=\"\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"fullScreen\" style=\"width:100% !important;\" width=\"600\">\r\n"
                + "                  <tr>\r\n"
                + "                    <td bgcolor=\"\" class=\"\" style=\"width:100% !important; padding: 0;\">\r\n"
                + "                      <custom type=\"content\" name=\"hero_image\">\r\n"
                + "                        <!--========Paste your Content below=================-->\r\n"
                + "\r\n"
                + "                        <!-- nothing -->\r\n"
                + "\r\n"
                + "                        <!--BEGIN HERO IMAGE -->\r\n"
                + "                        <table cellspacing=\"0\" cellpadding=\"0\" align=\"left\" border=\"0\" width=\"100%\" bgcolor=\"#ffffff\">\r\n"
                + "                          <tr>\r\n"
                + "                            <td valign=\"top\" align=\"center\" width=\"100%\" style=\"padding-right: 25px; padding-left: 25px;\">\r\n"
                + "                              <img width=\"100%\" style=\"max-width: 600px; height: auto\" src=\"https://s3.amazonaws.com/growth.lyft.com/Business/Images/Hero%20Images/Verifyemail_hero%402x.png\" alt=\"Lyft business profile\" />\r\n"
                + "                            </td>\r\n"
                + "                          </tr>\r\n"
                + "                        </table>\r\n"
                + "                        <!--END HERO IMAGE-->\r\n"
                + "\r\n"
                + "                        <table cellspacing=\"0\" cellpadding=\"0\" align=\"center\" border=\"0\" width=\"100%\" bgcolor=\"#F3F3F5\">\r\n"
                + "                          <tr>\r\n"
                + "                            <td align=\"center\" height=\"25px\" style=\"background-color: #FFFFFF;\">\r\n"
                + "                            </td>\r\n"
                + "                          </tr>\r\n"
                + "                        </table>\r\n"
                + "\r\n"
                + "                        <!--BEGIN TEXT SECTION-->\r\n"
                + "                        <table width=\"100%\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"max-width: 600px;\">\r\n"
                + "                          \r\n"
                + "                          </tr>\r\n"
                + "                          <tr>\r\n"
                + "                            <td style=\"font-family: 'LyftPro-Bold', Arial, Helvetica, sans-serif; font-size: 52px; line-height: 1.0; color: #000000; font-weight: bolder; padding: 0 25px 15px 25px;\" class=\"mso-line-solid mobile-headline\">\r\n"
                + title
                + "                            </td>\r\n"
                + "                          </tr>\r\n"
                + "                          <tr>\r\n"
                + "                            <td style=\"font-family: 'LyftPro-Regular', Arial, Helvetica, sans-serif; font-size: 18px; line-height: 1.4; color: #000000; padding: 0 25px 50px 25px;\">\r\n"
                + body
                + "                            </td>\r\n"
                + "                          </tr>\r\n"
                + "\r\n"
                + "                          <!-- CTA -->\r\n"
                + "                          <tr>\r\n"
                + "                            <td align=\"center\" style=\"padding: 0 25px 30px 25px; background-color: #ffffff;\">\r\n"
                + "                              <table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" class=\"full-width\">\r\n"
                + "                                <tr>\r\n"
                + "                                  <td class=\"cta-shadow\" align=\"center\" bgcolor=\"#5da4b4\"\" style=\"border-radius: 40px; -webkit-border-radius: 40px; -moz-border-radius: 40px;\">\r\n"
                + button
                + "                                  </td>\r\n"
                + "                                </tr>\r\n"
                + "                              </table>\r\n"
                + "                            </td>\r\n"
                + "                          </tr>\r\n"
                + "                        </table>\r\n"
                + "\r\n"
                + "                        <table cellspacing=\"0\" cellpadding=\"0\" align=\"center\" border=\"0\" width=\"100%\" bgcolor=\"#F3F3F5\">\r\n"
                + "                          <tr>\r\n"
                + "                            <td align=\"center\" height=\"25px\" style=\"background-color: #FFFFFF;\">\r\n"
                + "                            </td>\r\n"
                + "                          </tr>\r\n"
                + "                        </table>\r\n"
                + "                        <!--END TEXT SECTION -->\r\n"
                + "\r\n"
                + "                        <!--=======End your Content here=====================-->\r\n"
                + "                    </td>\r\n"
                + "                  </tr>\r\n"
                + "                </table>\r\n"
                + "\r\n"
                + "                \r\n"
                + "\r\n"
                + "                <!--=================FOOTER=====================-->\r\n"
                + "                <table align=\"center\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\" width:100% !important;\">\r\n"
                + "                  <tr>\r\n"
                + "                    <td align=\"center\" valign=\"middle\" style=\"padding: 0 25px 0 0;\"></td>\r\n"
                + "                    <td width=\"100%\" align=\"center\" valign=\"middle\" style=\"border-top: 1px solid #d8d8d8;\"></td>\r\n"
                + "                    <td align=\"center\" valign=\"middle\" style=\"padding: 0 25px 0 0;\"></td>\r\n"
                + "                  </tr>\r\n"
                + "                </table>\r\n"
                + "                <table align=\"center\" bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"mobileView\" style=\"width:100% !important;\" width=\"600\">\r\n"
                + "                  <tr>\r\n"
                + "                    <td>\r\n"
                + "                      <table align=\"center\" bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"mobileView\" style=\"\" width=\"100%\">\r\n"
                + "                        <tr>\r\n"
                + "                          <td align=\"left\" style=\"color: #88888f; font-family: 'LyftPro-Regular', 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 12px; line-height: 18px; font-weight:normal; padding: 10px 0px 0px 25px;\" valign=\"middle\" width=\"100%\">\r\n"
                + "                          </td>\r\n"
                + "                        </tr>\r\n"
                + "                        <tr>\r\n"
                + "                          <td align=\"left\" style=\"color: #88888f; font-family: 'LyftPro-Regular', 'Helvetica Neue', Helvetica, Arial, sans-serif; font-size: 12px; line-height:18px; font-weight: normal; padding: 10px 0px 0px 25px; text-decoration: none;\" valign=\"middle\">\r\n"
                + "                            185 FTN Street, Novi Sad 21000, Serbia<br/>&#169; 2022 Hakuna Matata, Inc.</td>\r\n"
                + "                        </tr>\r\n"
                + "                        <!--===========CUSTOMER ACTIONS===========-->\r\n"
                + "                      </table>\r\n"
                + "                      <!--=============END CUSTOMER ACTIONS========-->\r\n"
                + "                    </td>\r\n"
                + "                  </tr>\r\n"
                + "                  <tr>\r\n"
                + "                    <td width=\"auto\" style=\"display: block;\" height=\"40\">&nbsp;</td>\r\n"
                + "                  </tr>\r\n"
                + "                </table>\r\n"
                + "                <!--=================END FOOTER=====================-->\r\n"
                + "\r\n"
                + "              </td>\r\n"
                + "            </tr>\r\n"
                + "          </table>\r\n"
                + "          <!-- END WHITE PAGE BODY CONTAINER/WRAPPER -->\r\n"
                + "          <table cellspacing=\"0\" cellpadding=\"0\" align=\"center\" border=\"0\" width=\"100%\" bgcolor=\"#F3F3F5\">\r\n"
                + "            <tr>\r\n"
                + "              <td class=\"divider\" align=\"center\" height=\"16px\" style=\"background-color: #F3F3F5;\">\r\n"
                + "              </td>\r\n"
                + "            </tr>\r\n"
                + "          </table>\r\n"
                + "        </td>\r\n"
                + "      </tr>\r\n"
                + "    </table>\r\n"
                + "    <!-- FULL PAGE WIDTH WRAPPER WITH TINT -->\r\n"
                + "\r\n"
                + "    <!-- McGuyver'ed Android Gmail Spacer Fix -->\r\n"
                + "\r\n"
                + "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"hideDevice\">\r\n"
                + "      <tr>\r\n"
                + "        <td class=\"hideDevice\" height=\"1\" style=\"min-width:700px; font-size:0px; line-height:0px;\"><img height=\"1\" src=\"http://image.lyftmail.com/lib/fe6915707166047a7d14/m/8/Spacer+for+Gmail.gif\" style=\"min-width: 700px; text-decoration: none; border: none; -ms-interpolation-mode: bicubic;\"></td>\r\n"
                + "      </tr>\r\n"
                + "    </table>\r\n"
                + "\r\n"
                + "    <!--END FIX-->\r\n"
                + "\r\n"
                + "    <!-- RETURNPATH TRACKING -->\r\n"
                + "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\">\r\n"
                + "      <tr>\r\n"
                + "        <td border=\"0\" align=\"center\" height=\"1\" style=\"font-size:0px; line-height:0px;\">\r\n"
                + "          <img src=\"https://pixel.inbox.exacttarget.com/pixel.gif?r=4a4f30aa41389e0aba9e92c56574b86e3fc20465\" width=\"1\" height=\"1\" />\r\n"
                + "          <img src=\"https://pixel.app.returnpath.net/pixel.gif?r=4a4f30aa41389e0aba9e92c56574b86e3fc20465\" width=\"1\" height=\"1\" />\r\n"
                + "        </td>\r\n"
                + "      </tr>\r\n"
                + "    </table>\r\n"
                + "    <!-- END RETURNPATH  TRACKING -->\r\n"
                + "    <custom name=\"opencounter\" type=\"tracking\" style=\"display:none;\">\r\n"
                + "     \r\n"
                + "      <script data-cfasync=\"false\" src=\"/cdn-cgi/scripts/5c5dd728/cloudflare-static/email-decode.min.js\"></script></body>\r\n"
                + "\r\n"
                + "</html>";
        return content;
    }
}
