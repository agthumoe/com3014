<!DOCTYPE html>
<html class="no-js" lang="en">
<head>
    <%@include file="../template/head.jsp" %>
</head>
<%@include file="../template/browserupgrade.jsp" %>
<body>
<!-- Add your site or application content here -->
<jsp:include page="../template/navbar.jsp">
    <jsp:param name="active" value="help"/>
</jsp:include>
<div class="container">
    <h1 class="page-header">Help</h1>
    <h2>Registration</h2>
    <p>When first accessing the application the User will be asked to login. If not yet registered, a new user can
        create
        an account by selecting the "Register" option at the far right of the navigation bar at the top of the
        window.</p>
    <p>A registration form is displayed with the following fields:</p>
    <ul>
        <li>Username - required, must be unique, between 4 and 50 characters</li>
        <li>Password / Confirm Password - required, between 8 and 60 characters, user must enter and then confirm the
            same password
        </li>
        <li>Email - required, must enter a valid email address, up to 100 characters</li>
        <li>Name - optional, user may choose to provide their real first name or full name.</li>
    </ul>
    <p>Once complete, selecting the "Register" button at the bottom of the form will validate the entries and if the
        requirements have been met the new account will be created and the User returned to the "Login" screen.
        Otherwise an error will be displayed next to the field that could not be validated, explaining what changes to
        make in order to proceed.</p>
    <h2>Login</h2>
    <p>A User cannot access the game lobby until they have registered and logged in. An unregistered user will be
        presented with the "Login" screen immediately when accessing the site, however this can be returned to from any
        other screen by selecting the "Login" option from the bavigation bar at the top of the window.</p>
    <p>Entering a valid Username and Password and selecting the "Login" button below will allow the User access to the
        application.</p>
    <p>Should a User wish to logout at any time, this can be done by selecting their Username, now at the far right of
        the navigation bar at the top of the window, and selecting "Logout" at the bottom of the menu that appears.</p>
    <h2>Profile</h2>
    <p>A registered User can view their profile, update their details or change their password from a menu accessed by
        selecting their Username at the far right of the navigation bar at the top of the window. To view the user's
        profile, select "Profile", this will display their registered details along with the number of recorded wins,
        losses and their rating.</p>
    <p>To update the user's details, from the "Profile" screen, select "Update". A form will be displayed where changes
        can be made, the user's password must be re-enetered to confirm changes before selecting the "Update" button
        below to submit the changes.</p>
    <p>To change the user's password, select "Update Password", enter the current password, the new password and confirm
        it, then select the "Update Password" button below to submit the change.</p>
    <h2>Lobby</h2>
    <p>To access the game lobby select "Lobby" from the bavigation bar at the top of the window. In the lobby a Leader
        Board displays a list of users with the wins, losses and rating.</p>
    <p>Below is a chat box, messages from other users will be displayed beneath. To send a message, enter the message in
        the box and press Enter to send.</p>
    <p>Down the right is a list of other users also in the lobby.</p>
    <p>To initiate a game the User can select the lightning bolt "challenege" next to the player name that they wish to
        challenge, then they must wait for the other user to Accept or Deny the challenge.</p>
    <p>When receiving a challenge, a message box will appear letting the User know which user has challenged them, with
        the options to Accept or Deny this challenge. Accepting will launch the game.</p>
    <h2>Gameplay</h2>
    <p>Once a game has begun, the challenging user will be in control of the blue bike and the user being challeneged
        will be in control of the red bike.</p>
    <p>The bike can be rotated left and right by using the left and right cursor keys respectively. Pressing the up
        cursor will accelerate the bike forward, releasing will allow it to slow to a halt. Pressing the space bar will
        fire a projectile from the front of the bike.</p>
    <p>The object of the game is to destroy the opponent's bike by hitting it with a projectile. This will result in a
        win for the survivor and a loss for their opponent, and a corresponding change to both users' ratings.</p>
    <p>After the game ends both users are returned to the game lobby.</p>
    <h2>Admin Actions</h2>
    <h3>User Management</h3>
    <p>When an Admin User logs in, instead of being taken to the game lobby they are initially taken to the "User
        Dashboard" screen. This can also be accessed by selecting their Username at the far right of the navigation bar
        at the top of the window and selecting "Dashboard" in the menu that appears.</p>
    <p>From the user dashboard and Admin can view all user details, search for users by Username, Email, Name, Status
        and Role, create new useres, as well as delete, edit, promote/demote to admin, or ban/unban any existing
        user.</p>
    <p>To filter the users displayed the Admin can select an attribute to filter by from the drop down, enter a text or
        part text to filter by, enter a limit of number of users to return, and select the "Filter" icon to submit.
        Selecting the "Refresh" icon will return to displaying all users.</p>
    <p>To create a user the Admin can select the green "Add" icon, enter all details for the new user account, and
        select the "Create" button at the bottom.</p>
    <p>Selecting the "Delete" icon next to a user will prompt to confirm deletion, confirming will remove the user
        account from the system.</p>
    <p>Selecting the "Edit" icon allows the Admin to change the users Email, Name and Password, and to promote their
        Authority to Admin or demote to User. Unticking "Enable" will ban the user from the system, reticking will
        reactivate the user. Changes will be applied once the "Update" button at the bottom is selected.</p>
    <h3>API Interface</h3>
    <p>Selecting "Swagger UI" from the footer bar at the bottom of the page allows an Admin user to access the API page.
        From here a list of operations accessible via the application's API can viewed and understood. Selecting
        "Show/Hide" next to a section will expand or collapse the list of operations for that section, these can be
        expanded further for full details, or collapsed back to a simple list.</p>
</div>
<div>
</div>
<%@include file="../template/footer.jsp" %>
<%@include file="../template/scripts.jsp" %>
</body>
</html>
