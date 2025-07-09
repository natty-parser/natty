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
	_submit: function () {

		this._summary.hide();
		this._date.hide();
		this._error.hide();
		this._empty.hide();

		if (this._input.value.strip().length === 0) {
			this._empty.show();
			return;
		}

		this._loadingIndicator.show();
		this._structureDetails.update('');
	}
});

Event.observe(window, 'load', function() {
  new Form();
});
