class Position
	def initialize
		@depth = 0
		@horizPosition = 0
		@aim = 0
	end

    def depth
	@depth
    end

    def horizPosition
	@horizPosition
    end

    def aim
	@aim
    end

    def setDepth(newDepth)
	@depth = newDepth
    end

    def setHorizPosition(newHorizPosition)
	@horizPosition = newHorizPosition
    end

    def setAim(newAim)
	@aim = newAim
    end

    def initialize_copy(other_obj)
	super
    end

    def print
	puts "Depth: #{@depth}"
	puts "Horiz pos: #{@horizPosition}"
	puts "Aim: #{@aim}"
    end
end

def parseCommands(posObj, direction, unit)
    retVal = posObj.dup
    if direction == "forward"
        retVal.setHorizPosition(posObj.horizPosition + unit)
	retVal.setDepth(posObj.depth + (posObj.aim * unit))
    elsif direction == "down"
        retVal.setAim(posObj.aim + unit)
    elsif direction == "up"
        retVal.setAim(posObj.aim - unit)
    end
    retVal
end

input = File.open("input.txt")
inputLines = input.readlines.map(&:chomp)
currentPos = Position.new
for il in inputLines
	split = il.split(' ')
	currentPos = parseCommands(currentPos, split[0], split[1].to_i)
end
puts "Solution is: #{currentPos.depth * currentPos.horizPosition}"
input.close

