# FeatureIPLMatch
This app taken list of IPL matches from user and creates a feature list out of them in random order. Then when user proceeds in app, automatically random teams are make winner until one team wins. Finally displays the winner name.


Home Page: 

<img src="https://user-images.githubusercontent.com/13178473/155842607-67d7ccfc-ee13-482a-b9fd-ecec14c90154.jpg" width="250" height="500">

List of Teams: 

<img src="https://user-images.githubusercontent.com/13178473/155842609-99b65b8c-4264-43e1-b59b-dadbeddd836f.jpg" width="250" height="500"> 

Randomized feature list : 

<img src="https://user-images.githubusercontent.com/13178473/155842605-799e4cd5-aa18-4323-88ec-308cd2849a40.jpg" width="250" height="500">

Final match between : 

<img src="https://user-images.githubusercontent.com/13178473/155842600-f7fe7db7-4376-4aae-984c-baefa6b908dd.jpg" width="250" height="500">     

Result page : <img src="https://user-images.githubusercontent.com/13178473/155842608-a17dd96d-4676-4782-a37e-3345e701e1b6.jpg" width="250" height="500">

1. Implemented features:
	1. Home : 


			1. Shows option to populate hard coded list of 8 teams or lets user enter m number of teams.
			2. Click on start ipl to proceed for feature creation, where team pairs will be created in random order.
	2. Team feature list :


			1. Shows list of feature teams to play match.
			2. On click of simulate, at random one team is defeated among each pairs and another feature based on winner teams is created.
			3. This process continues until final two teams are left.
	3. Result page:


			1. Shows winner team names in order of 1st , 2nd and 3rd.
			2. There is also a button to restart the match from start.

2.  App uses:
		
		1. MVVM
		2. Recycler view
		3. Data binding
		4. Activity-Fragment communication, fragment-fragment communication
		5. Live-MutableLive data
		6.Room Db
