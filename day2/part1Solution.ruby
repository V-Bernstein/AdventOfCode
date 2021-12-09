class Position
	def initialize
		@depth = 0
		@horizPosition = 0
	end

    def depth
	@depth
    end

    def horizPosition
	@horizPosition
    end

    def setDepth(newDepth)
	@depth = newDepth
    end

    def setHorizPosition(newHorizPosition)
	@horizPosition = newHorizPosition
    end

    def initialize_copy(other_obj)
	super
    end

    def print
	puts "Depth: #{@depth}"
	puts "Horiz pos: #{@horizPosition}"
    end
end

def parseCommands(posObj, direction, unit)
    retVal = posObj.dup
    if direction == "forward"
        retVal.setHorizPosition(posObj.horizPosition + unit)
    elsif direction == "down"
        retVal.setDepth(posObj.depth + unit)
    elsif direction == "up"
        retVal.setDepth(posObj.depth - unit)
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

