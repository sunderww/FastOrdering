test:
	./node_modules/.bin/mocha  --reporter spec --colors --recursive test --timeout 50000

.PHONY: test