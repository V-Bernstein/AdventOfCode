input = File.read("input.txt").split
slides = Array.new
idx = 1
while idx < input.length-1 
	slides.push(input[idx-1].to_i + input[idx].to_i + input[idx+1].to_i)
	idx += 1
end 
increaseCount = 0
idx = 1
while idx < slides.length
	if slides[idx] > slides[idx - 1]
		increaseCount += 1
	end
	idx += 1
end
puts "Solution is: #{increaseCount}"
