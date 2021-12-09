input = File.read("input.txt").split
increaseCount = 0
previousLine = input[0]
for line in input
	if line >= previousLine
		increaseCount += 1
	end
	previousLine = line
end
puts "Solution is: #{increaseCount}"
