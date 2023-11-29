package com.example.gestion_achat3.service;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import org.apache.commons.codec.binary.Base64;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class MailService {
    public static final String HOST ="http://localhost:8080/";
    private static final String APPLICATION_NAME = "Appli de gestino commercial";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    public final Gmail SERVICE = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();

    public MailService() throws IOException, GeneralSecurityException {
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = MailService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        //returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    public void sendMessage(String userId, MimeMessage emailContent)
            throws MessagingException, IOException {
        Message message = createMessageWithEmail(emailContent);
        SERVICE.users().messages().send(userId, message).execute();
    }

    public MimeMessage createEmail(String to, String from, String subject, String bodyText)
            throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from, "Gestion commerical App"));
        email.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setContent(bodyText, "text/html");
        return email;
    }

    public void sendProforma(String to, String url) throws MessagingException, IOException {
        String bodyText = """
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
                <html xmlns="http://www.w3.org/1999/xhtml">
                	<head>
                		<title></title>
                		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
                		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
                		<style type="text/css">
                			* {
                				-ms-text-size-adjust:100%;
                				-webkit-text-size-adjust:none;
                				-webkit-text-resize:100%;
                				text-resize:100%;
                			}
                			a{
                				outline:none;
                				color:#40aceb;
                				text-decoration:underline;
                			}
                			a:hover{text-decoration:none !important;}
                			.nav a:hover{text-decoration:underline !important;}
                			.title a:hover{text-decoration:underline !important;}
                			.title-2 a:hover{text-decoration:underline !important;}
                			.btn:hover{opacity:0.8;}
                			.btn a:hover{text-decoration:none !important;}
                			.btn{
                				-webkit-transition:all 0.3s ease;
                				-moz-transition:all 0.3s ease;
                				-ms-transition:all 0.3s ease;
                				transition:all 0.3s ease;
                			}
                			table td {border-collapse: collapse !important;}
                			.ExternalClass, .ExternalClass a, .ExternalClass span, .ExternalClass b, .ExternalClass br, .ExternalClass p, .ExternalClass div{line-height:inherit;}
                			@media only screen and (max-width:500px) {
                				table[class="flexible"]{width:100% !important;}
                				table[class="center"]{
                					float:none !important;
                					margin:0 auto !important;
                				}
                				*[class="hide"]{
                					display:none !important;
                					width:0 !important;
                					height:0 !important;
                					padding:0 !important;
                					font-size:0 !important;
                					line-height:0 !important;
                				}
                				td[class="img-flex"] img{
                					width:100% !important;
                					height:auto !important;
                				}
                				td[class="aligncenter"]{text-align:center !important;}
                				th[class="flex"]{
                					display:block !important;
                					width:100% !important;
                				}
                				td[class="wrapper"]{padding:0 !important;}
                				td[class="holder"]{padding:30px 15px 20px !important;}
                				td[class="nav"]{
                					padding:20px 0 0 !important;
                					text-align:center !important;
                				}
                				td[class="h-auto"]{height:auto !important;}
                				td[class="description"]{padding:30px 20px !important;}
                				td[class="i-120"] img{
                					width:120px !important;
                					height:auto !important;
                				}
                				td[class="footer"]{padding:5px 20px 20px !important;}
                				td[class="footer"] td[class="aligncenter"]{
                					line-height:25px !important;
                					padding:20px 0 0 !important;
                				}
                				tr[class="table-holder"]{
                					display:table !important;
                					width:100% !important;
                				}
                				th[class="thead"]{display:table-header-group !important; width:100% !important;}
                				th[class="tfoot"]{display:table-footer-group !important; width:100% !important;}
                			}
                		</style>
                	</head>
                	<body style="margin:0; padding:0;" bgcolor="#eaeced">
                		<table style="min-width:320px;" width="100%" cellspacing="0" cellpadding="0" bgcolor="#eaeced">
                			<!-- fix for gmail -->
                			<tr>
                				<td class="hide">
                					<table width="600" cellpadding="0" cellspacing="0" style="width:600px !important;">
                						<tr>
                							<td style="min-width:600px; font-size:0; line-height:0;">&nbsp;</td>
                						</tr>
                					</table>
                				</td>
                			</tr>
                			<tr>
                				<td class="wrapper" style="padding:0 10px;">
                					<!-- module 2 -->
                     					<table data-module="module-2" data-thumb="thumbnails/02.png" width="100%" cellpadding="0" cellspacing="0">
                     						<tr>
                     							<td data-bgcolor="bg-module" bgcolor="#eaeced">
                     								<table class="flexible" width="600" align="center" style="margin:0 auto;" cellpadding="0" cellspacing="0">
                     									<tr>
                     										<td class="img-flex"><img src="https://s6.imgcdn.dev/VujIe.jpg" style="vertical-align:top;" width="600" height="306" alt="" /></td>
                     									</tr>
                     									<tr>
                     										<td data-bgcolor="bg-block" class="holder" style="padding:58px 60px 52px;" bgcolor="#f9f9f9">
                     											<table width="100%" cellpadding="0" cellspacing="0">
                     												<tr>
                     													<td data-color="title" data-size="size title" data-min="25" data-max="45" data-link-color="link title color" data-link-style="text-decoration:none; color:#292c34;" class="title" align="center" style="font:35px/38px Arial, Helvetica, sans-serif; color:#292c34; padding:0 0 24px;">
                     														Bonjour :),
                     													</td>
                     												</tr>
                     												<tr>
                     													<td data-color="text" data-size="size text" data-min="10" data-max="26" data-link-color="link text color" data-link-style="font-weight:bold; text-decoration:underline; color:#40aceb;" align="center" style="font:bold 16px/25px Arial, Helvetica, sans-serif; color:#888; padding:0 0 23px;">
                     														Nous sommes actuellement en phase de planification et sommes intéressés par vos produits/services en raison de votre réputation dans le secteur. Afin de poursuivre notre processus de prise de décision, pourriez-vous s'il vous plaît nous envoyer un devis proforma sur notre site:
                     													</td>
                     												</tr>
                     												<tr>
                     													<td style="padding:0 0 20px;">
                     														<table width="134" align="center" style="margin:0 auto;" cellpadding="0" cellspacing="0">
                     															<tr>
                     																<td data-bgcolor="bg-button" data-size="size button" data-min="10" data-max="16" class="btn" align="center" style="font:12px/14px Arial, Helvetica, sans-serif; color:#f8f9fb; text-transform:uppercase; mso-padding-alt:12px 10px 10px; border-radius:2px;" bgcolor="#7bb84f">
                     																	<a target="_blank" style="text-decoration:none; color:#f8f9fb; display:block; padding:12px 10px 10px;" href='"""+url+"""
                                                                                        '>Faire le proforma</a>
                     																</td>
                     															</tr>
                     														</table>
                     													</td>
                     												</tr>
                     											</table>
                     										</td>
                     									</tr>
                     									<tr><td height="28"></td></tr>
                     								</table>
                     							</td>
                     						</tr>
                     					</table>
                					<!-- module 7 -->
                					<table data-module="module-7" data-thumb="thumbnails/07.png" width="100%" cellpadding="0" cellspacing="0">
                						<tr>
                							<td data-bgcolor="bg-module" bgcolor="#eaeced">
                								<table class="flexible" width="600" align="center" style="margin:0 auto;" cellpadding="0" cellspacing="0">
                									<tr>
                										<td class="footer" style="padding:0 0 10px;">
                											<table width="100%" cellpadding="0" cellspacing="0">
                												<tr class="table-holder">
                													<th class="tfoot" width="400" align="left" style="vertical-align:top; padding:0;">
                														<table width="100%" cellpadding="0" cellspacing="0">
                															<tr>
                																<td data-color="text" data-link-color="link text color" data-link-style="text-decoration:underline; color:#797c82;" class="aligncenter" style="font:12px/16px Arial, Helvetica, sans-serif; color:#797c82; padding:0 0 10px;">
                																	Notre compagnie, 2023. &nbsp; All Rights Reserved. <a target="_blank" style="text-decoration:underline; color:#797c82;" href="">0 20 (22) 123 45</a>
                																</td>
                															</tr>
                														</table>
                													</th>
                												</tr>
                											</table>
                										</td>
                									</tr>
                								</table>
                							</td>
                						</tr>
                					</table>
                				</td>
                			</tr>
                			<!-- fix for gmail -->
                			<tr>
                				<td style="line-height:0;"><div style="display:none; white-space:nowrap; font:15px/1px courier;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div></td>
                			</tr>
                		</table>
                	</body>
                </html>
                """;
        MimeMessage emailContent = createEmail(to, "gestion-commercial@example.com", "Demande devis PROFORMA", bodyText);
        sendMessage("me", emailContent);
    }

    public void sendBonDeCommande(String to, String url) throws MessagingException, IOException {
        String bodyText = """
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
                <html xmlns="http://www.w3.org/1999/xhtml">
                	<head>
                		<title></title>
                		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
                		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
                		<style type="text/css">
                			* {
                				-ms-text-size-adjust:100%;
                				-webkit-text-size-adjust:none;
                				-webkit-text-resize:100%;
                				text-resize:100%;
                			}
                			a{
                				outline:none;
                				color:#40aceb;
                				text-decoration:underline;
                			}
                			a:hover{text-decoration:none !important;}
                			.nav a:hover{text-decoration:underline !important;}
                			.title a:hover{text-decoration:underline !important;}
                			.title-2 a:hover{text-decoration:underline !important;}
                			.btn:hover{opacity:0.8;}
                			.btn a:hover{text-decoration:none !important;}
                			.btn{
                				-webkit-transition:all 0.3s ease;
                				-moz-transition:all 0.3s ease;
                				-ms-transition:all 0.3s ease;
                				transition:all 0.3s ease;
                			}
                			table td {border-collapse: collapse !important;}
                			.ExternalClass, .ExternalClass a, .ExternalClass span, .ExternalClass b, .ExternalClass br, .ExternalClass p, .ExternalClass div{line-height:inherit;}
                			@media only screen and (max-width:500px) {
                				table[class="flexible"]{width:100% !important;}
                				table[class="center"]{
                					float:none !important;
                					margin:0 auto !important;
                				}
                				*[class="hide"]{
                					display:none !important;
                					width:0 !important;
                					height:0 !important;
                					padding:0 !important;
                					font-size:0 !important;
                					line-height:0 !important;
                				}
                				td[class="img-flex"] img{
                					width:100% !important;
                					height:auto !important;
                				}
                				td[class="aligncenter"]{text-align:center !important;}
                				th[class="flex"]{
                					display:block !important;
                					width:100% !important;
                				}
                				td[class="wrapper"]{padding:0 !important;}
                				td[class="holder"]{padding:30px 15px 20px !important;}
                				td[class="nav"]{
                					padding:20px 0 0 !important;
                					text-align:center !important;
                				}
                				td[class="h-auto"]{height:auto !important;}
                				td[class="description"]{padding:30px 20px !important;}
                				td[class="i-120"] img{
                					width:120px !important;
                					height:auto !important;
                				}
                				td[class="footer"]{padding:5px 20px 20px !important;}
                				td[class="footer"] td[class="aligncenter"]{
                					line-height:25px !important;
                					padding:20px 0 0 !important;
                				}
                				tr[class="table-holder"]{
                					display:table !important;
                					width:100% !important;
                				}
                				th[class="thead"]{display:table-header-group !important; width:100% !important;}
                				th[class="tfoot"]{display:table-footer-group !important; width:100% !important;}
                			}
                		</style>
                	</head>
                	<body style="margin:0; padding:0;" bgcolor="#eaeced">
                		<table style="min-width:320px;" width="100%" cellspacing="0" cellpadding="0" bgcolor="#eaeced">
                			<!-- fix for gmail -->
                			<tr>
                				<td class="hide">
                					<table width="600" cellpadding="0" cellspacing="0" style="width:600px !important;">
                						<tr>
                							<td style="min-width:600px; font-size:0; line-height:0;">&nbsp;</td>
                						</tr>
                					</table>
                				</td>
                			</tr>
                			<tr>
                				<td class="wrapper" style="padding:0 10px;">
                				    <!-- module 6 -->
                    				<table data-module="module-6" data-thumb="thumbnails/06.png" width="100%" cellpadding="0" cellspacing="0">
                    						<tr>
                    							<td data-bgcolor="bg-module" bgcolor="#eaeced">
                    								<table class="flexible" width="600" align="center" style="margin:0 auto;" cellpadding="0" cellspacing="0">
                    									<tr>
                    										<td data-bgcolor="bg-block" class="holder" style="padding:64px 60px 50px;" bgcolor="#f9f9f9">
                    											<table width="100%" cellpadding="0" cellspacing="0">
                    												<tr>
                    													<td data-color="title" data-size="size title" data-min="20" data-max="40" data-link-color="link title color" data-link-style="text-decoration:none; color:#292c34;" class="title" align="center" style="font:30px/33px Arial, Helvetica, sans-serif; color:#292c34; padding:0 0 23px;">
                    														Bon de commande
                    													</td>
                    												</tr>
                    												<tr>
                    													<td data-color="text" data-size="size text" data-min="10" data-max="26" data-link-color="link text color" data-link-style="font-weight:bold; text-decoration:underline; color:#40aceb;" align="center" style="font:16px/29px Arial, Helvetica, sans-serif; color:#888; padding:0 0 21px;">
                    														Nous sommes fier de travailler avec vous, voici notre bon de commande:
                    													</td>
                    												</tr>
                    												<tr>
                    													<td style="padding:0 0 20px;">
                    														<table width="232" align="center" style="margin:0 auto;" cellpadding="0" cellspacing="0">
                    															<tr>
                    																<td data-bgcolor="bg-button" data-size="size button" data-min="10" data-max="20" class="btn" align="center" style="font:bold 16px/18px Arial, Helvetica, sans-serif; color:#f9f9f9; text-transform:uppercase; mso-padding-alt:22px 10px; border-radius:3px;" bgcolor="#e02d74">
                    																	<a target="_blank" style="text-decoration:none; color:#f9f9f9; display:block; padding:22px 10px;" href='"""+url+"""
                                                                                        '>Voir le bon de commande</a>
                    																</td>
                    															</tr>
                    														</table>
                    													</td>
                    												</tr>
                    											</table>
                    										</td>
                    									</tr>
                    									<tr><td height="28"></td></tr>
                    								</table>
                    							</td>
                    						</tr>
                    				</table>
                					<!-- module 7 -->
                					<table data-module="module-7" data-thumb="thumbnails/07.png" width="100%" cellpadding="0" cellspacing="0">
                						<tr>
                							<td data-bgcolor="bg-module" bgcolor="#eaeced">
                								<table class="flexible" width="600" align="center" style="margin:0 auto;" cellpadding="0" cellspacing="0">
                									<tr>
                										<td class="footer" style="padding:0 0 10px;">
                											<table width="100%" cellpadding="0" cellspacing="0">
                												<tr class="table-holder">
                													<th class="tfoot" width="400" align="left" style="vertical-align:top; padding:0;">
                														<table width="100%" cellpadding="0" cellspacing="0">
                															<tr>
                																<td data-color="text" data-link-color="link text color" data-link-style="text-decoration:underline; color:#797c82;" class="aligncenter" style="font:12px/16px Arial, Helvetica, sans-serif; color:#797c82; padding:0 0 10px;">
                																	Notre compagnie, 2023. &nbsp; All Rights Reserved. <a target="_blank" style="text-decoration:underline; color:#797c82;" href="">0 20 (22) 123 45</a>
                																</td>
                															</tr>
                														</table>
                													</th>
                												</tr>
                											</table>
                										</td>
                									</tr>
                								</table>
                							</td>
                						</tr>
                					</table>
                				</td>
                			</tr>
                			<!-- fix for gmail -->
                			<tr>
                				<td style="line-height:0;"><div style="display:none; white-space:nowrap; font:15px/1px courier;">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</div></td>
                			</tr>
                		</table>
                	</body>
                </html>
                """;
        MimeMessage emailContent = createEmail(to, "gestion-commercial@example.com", "Bon de commande", bodyText);
        sendMessage("me", emailContent);
    }
}
