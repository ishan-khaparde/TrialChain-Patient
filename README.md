-AliasActivity: An activity presented to the user to take alias input
 Has a text field and a button. On button press, a keypair is generated and is associated with the alias. Then the alias and the key pair are stored in a SQLite database.

-DBHelper: A helper class that manages all transactions(read and write) with the database. This has methods for adding new keypairs, retrieving keys based on alias, deleting keys and so on.

-KeyAdapter: This is the adapter class for the list view on the record list. This adapter is responsible for populating the list with the data from the SQLite database. The adapter is also responsible for instantiating the layout (initializing the text labels and filling them with alias and public keys) for each row of the listview.

-KeyPairAlias: A plain java class that bundles alias, public key and secret keys together. This is designed so that handling all three becomes easier. There is no functionality provided here, the class just declares data items and has getters and setters.

-LoginActivity: This is the launcher activity of the application. This presents the user with a text field where the user is supposed to enter their PIN. The activity, when first launched searches for existing credentials. If no stored PIN is found, the user is redirected to a register activity, where they can set up their PIN.

-MainActivity: This activity is the primary dashboard activity of the patient application. This activity encompasses a pager view,that puts up two fragments. First one as a record list and second as a messaging interface. This activity is largely a navigation controller, the data is controlled by the fragments.

-PagerFragment: This is the fragment class that the MainActivity encompasses. The fragment class populates different layout depending on what fragment is in focus. Fragment 1 is a listview and the messaging interface is yet to finish. It will probably be a list of card views, each card view will have notification for the patient( this is the idea, I have not started this yet). The PagerFragment.java file also defines a PagerAdapter, that just like the KeyAdapter is responsible for inflating layouts for fragment and filling in the content.

-QRCodeDisplayActivity: This is invoked when the user selects an item from the listview. The select item’s public key is then passed to this activity as an argument and a QR Code is prepared for it. Lastly, an imageview on this activity displays the generated bit map.

-QRCodeScannerActivity: This brings up the camera to scan a QRCode. This activity reads a QR Code and just makes a toast of it at the moment. 

-RegisterActivity: So this is the activity where the user can setup their PIN on first run of the application. It writes the PIN to a preferences file, and is stored as a key value pair.

-Token: This is the token that will be signed and posted to the URL embedding in the QR code on the web interface. Again, it just comprises of a public key and a timestamp, and getters and setters for it. Once the web ui has a QR code interface up, I may modify the class as needed.
