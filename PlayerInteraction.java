public class PlayerInteraction {

    private PlayerMovement player;

    public PlayerInteraction(PlayerMovement player) {
        this.player = player;
    }

    // Inner class to represent a generic GameObject
    public abstract class GameObject {
        private int x, y;

        public GameObject(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public abstract void interact(PlayerMovement player);
    }


    public boolean collisionChecker(GameObject obj) {
        return player.getPlayerX() == obj.getX() && player.getPlayerY() == obj.getY(); 
    }
    
}
