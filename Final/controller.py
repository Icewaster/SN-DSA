import pygame
from model import GameModel
from view import GameView


class GameController:

    def __init__(self, width, height, cell_size):
        self.model = GameModel(width, height)
        self.view = GameView(self.model, cell_size)
        self.clock = pygame.time.Clock()
        self.running = True

    def run(self):
        while self.running:
            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    self.running = False

            self.view.render()

            self.clock.tick(60)

        pygame.quit()