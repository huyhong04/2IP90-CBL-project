public class PlayerInteraction {

    private PlayerMovement player;

    public PlayerInteraction(PlayerMovement player) {
        this.player = player;
    }
    
    public boolean collisionChecker(GameObject obj) {
        return player.getPlayerX() == obj.getX() && player.getPlayerY() == obj.getY(); 
    }
    
}
