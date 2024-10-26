public class PlayerInteraction {

    private PlayerMovement player;

    /**
     * Initializes the PlayerInteraction with a PlayerMovement instance.
     *
     * @param player the PlayerMovement instance representing the player in the game.
     */
    public PlayerInteraction(PlayerMovement player) {
        this.player = player;
    }

    /**
     * Checks for a collision between the player and a specified game object.
     * Compares the player's position with the object's position to detect overlap.
     *
     * @param obj the GameObject to check for a collision with the player.
     * @return true if the player is in the same position as the object, false otherwise.
     */
    public boolean collisionChecker(GameObject obj) {
        return player.getPlayerX() == obj.getX() && player.getPlayerY() == obj.getY(); 
    }
    
}
