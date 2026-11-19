import pygame


class GameView:

    def __init__(self, model, cell_size):
        self.model = model
        self.cell_size = cell_size

        self.window_width = self.model.board.width * self.cell_size
        self.window_height = self.model.board.height * self.cell_size

        pygame.init()
        self.screen = pygame.display.set_mode(
            (self.window_width, self.window_height)
        )
        pygame.display.set_caption("Snake")

    def draw_grid(self):
        grid_color = (60, 60, 60)

        for x in range(0, self.window_width, self.cell_size):
            pygame.draw.line(self.screen, grid_color, (x, 0), (x, self.window_height))

        for y in range(0, self.window_height, self.cell_size):
            pygame.draw.line(self.screen, grid_color, (0, y), (self.window_width, y))

    def render(self):
        self.screen.fill((15, 15, 15)) 
        self.draw_grid()
        
        pygame.display.flip()