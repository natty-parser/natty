Form = Class.create({
	initialize: function () {
		this._input = $('text_input');
		this._loadingIndicator = $('loading');
		this._date = $('date');
		this._summary = $('summary');
		this._error = $('error');
		this._empty = $('empty');
		this._structureDetails = $('structure_details');
		this._astDetails = $('ast_details');

		// submit on enter in text field
		this._input.observe('keypress', function (e) {
			if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) this._submit();
		}.bind(this));

		// or when the submit button is pressed
		$('submit').observe('click', this._submit.bind(this));

		// focus on the input by default
		this._input.focus();
		this._input.select();
	},

	/**
	 *
	 */
	_submit: async function () {

		this._summary.hide();
		this._date.hide();
		this._error.hide();
		this._empty.hide();
		if (this._input.value.strip().length === 0) {
			this._empty.show();
			return;
		}
		this._loadingIndicator.show();
		try {
			const value = this._input.value.strip();
			console.log("Parsing value:", value);
			const result = await window.nattyParser.parse(value);
			if (await result.isEmpty()) {
				console.log("No result found");
				this._empty.show();
			} else {
				const firstGroup = await result.get(0);
				const syntaxTree = await firstGroup.getSyntaxTree();
				const syntaxTreeString = await syntaxTree.toStringTree();
				const dates = await firstGroup.getDates();
				const firstDate = await dates.get(0);
				const string = await firstDate.toString();
				this._date.update(string);
				this._structureDetails.update(syntaxTreeString);
				this._summary.show();
			}
		} catch (e) {
			this._date.update("No date found");
			this._error.update(await e.toString());
			this._error.show();
		}
		this._date.show();
		this._loadingIndicator.hide();
	}
}
);

Event.observe(window, 'load', function() {
  new Form();
});
