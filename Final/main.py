from controller import GameController

WIDTH = 16
HEIGHT = 18
CELL_SIZE = 30

def main():
    controller = GameController(WIDTH, HEIGHT, CELL_SIZE)
    controller.run()


if __name__ == "__main__":
    main()