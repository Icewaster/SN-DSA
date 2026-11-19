import random


class Board:

    def __init__(self, width, height):
        self.width = width
        self.height = height

    def is_within_bounds(self, pos):
        x, y = pos
        return 0 <= x < self.width and 0 <= y < self.height


class Food:

    def __init__(self, board):
        self.board = board
        self.position = (0, 0)

    def respawn(self, snake_body): 
        cells = [(x, y) for x in range(self.board.width) for y in range(self.board.height)]
        available_cells = []

        for cell in cells:
            if cell not in snake_body:
                available_cells.append(cell)

        if len(available_cells) == 0:
            self.position = None # flag for win condition
            return

        self.position = random.choice(available_cells)


class Snake:

    def __init__(self, board):
        self.board = board
        self.body = [(board.width // 2, board.height // 2)]
        self.direction = "east"

    def move(self, grow = False):

        head_x, head_y = self.body[0]

        if self.direction == "east":
            new_head = (head_x + 1, head_y)
        elif self.direction == "west":
            new_head = (head_x - 1, head_y)
        elif self.direction == "north":
            new_head = (head_x, head_y - 1)
        elif self.direction == "south":
            new_head = (head_x, head_y + 1)

        self.body.insert(0, new_head)

        if not grow:
            self.body.pop()

    def self_collision(self):
        return self.body[0] in self.body[1:]


class GameModel:

    def __init__(self, width, height):
        self.board = Board(width, height)