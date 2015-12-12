//
//  EditDishViewController.m
//  FastOrdering
//
//  Created by Sunder on 06/10/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "EditDishViewController.h"
#import "ReviewExpandableCell.h"
#import "OptionCategory.h"
#import "Option.h"
#import "OptionCell.h"
#import "OrderedOption.h"
#import "Dish.h"
#import "NSManagedObject+create.h"
#import "OrderedDish+Custom.h"
#import "AppDelegate.h"

@interface EditDishViewController ()

@end

#define kOptionCellTag(section, row)	((((section) + 1) * 100) + (row) + 1)
#define kOptionCellSectionForTag(tag)	(((tag) / 100) - 1)
#define kOptionCellRowForTag(tag)		(((tag) % 100) - 1)

@implementation EditDishViewController

- (void)viewDidLoad {
    [super viewDidLoad];
	
//	[((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext.undoManager beginUndoGrouping];
	
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(keyboardWillShow:)
												 name:UIKeyboardWillShowNotification
											   object:nil];
 
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(keyboardWillHide:)
												 name:UIKeyboardWillHideNotification
											   object:nil];
	
	[self reloadData];
	
	commentView.text = self.dish.comment;
	commentLabel.text = NSLocalizedString(commentLabel.text, @"");
	[validateButton setTitle:NSLocalizedString(@"validate", @"").uppercaseString forState:UIControlStateNormal];
	[backButton setTitle:NSLocalizedString(@"back", @"").capitalizedString forState:UIControlStateNormal];
}

- (void)viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
	[[NSNotificationCenter defaultCenter] removeObserver:self.view];
//	
//	NSManagedObjectContext * context = ((AppDelegate *)UIApplication.sharedApplication.delegate).managedObjectContext;
//	[context.undoManager endUndoGrouping];
//	if (!saved) {
//		[context.undoManager undoNestedGroup];
//	}
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)reloadData {
	NSMutableArray * tmp = [NSMutableArray new];
	NSMutableArray * allOptionsTmp = [NSMutableArray new];

	categories = self.dish.dish.optioncategories.allObjects;
	for (OptionCategory * category in categories) {
		NSMutableArray * orderedOptions = [NSMutableArray new];
		for (Option * option in category.option.allObjects) {
			OrderedOption * ordered = [self.dish orderedOptionWithOption:option];
			if (!ordered) {
				ordered = [OrderedOption create];
				ordered.option = option;
				// Don't set the OrderedDish yet (because we could not cancel)
				ordered.qty = 0;
			}
			[orderedOptions addObject:ordered];
			[allOptionsTmp addObject:ordered];
		}
		[tmp addObject:orderedOptions];
	}
	options = tmp;
	allOptions = allOptionsTmp;
	[expandableTableView reloadData];
	
	if (!allOptions.count) {
		commentSuperview.frame = commentPlaceholder.frame;
		expandableTableView.hidden = YES;
	}
}

#pragma mark - Keyboard methods

- (void)keyboardWillShow:(NSNotification *)notification {
	keyboardSize = [[[notification userInfo] objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
}

- (void)keyboardWillHide:(NSNotification *)notification {
	keyboardSize = [[[notification userInfo] objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
}

#pragma mark - IBAction methods

- (IBAction)endEditing {
	[self.view endEditing:YES];
}

- (IBAction)validate {
	saved = YES;

	for (OrderedOption * option in allOptions) {
		option.orderedDish = self.dish;
	}
	[self.dish sanitize];
	self.dish.comment = commentView.text;
	[self.delegate popEditDishView];
}

- (IBAction)cancel {
	[self.delegate popEditDishView];
}

#pragma mark - SLExpandableTableView delegate and datasource methods

- (BOOL)tableView:(SLExpandableTableView *)tableView canExpandSection:(NSInteger)section {
	return YES;
}

- (BOOL)tableView:(SLExpandableTableView *)tableView needsToDownloadDataForExpandableSection:(NSInteger)section {
	return NO;
}

- (UITableViewCell<UIExpandingTableViewCell> *)tableView:(SLExpandableTableView *)tableView expandingCellForSection:(NSInteger)section {
	static NSString * CellIdentifier = @"EditDishTitleCell";
	
	ReviewExpandableCell * cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	
	if (!cell) {
		cell = [[ReviewExpandableCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
	}
	
	cell.text = ((OptionCategory *)categories[section]).name;
	return cell;
}

- (void)tableView:(SLExpandableTableView *)tableView downloadDataForExpandableSection:(NSInteger)section {
	[tableView expandSection:section animated:YES];
}

#pragma mark - UITableView delegate and datasource methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	return categories.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
	return ((NSArray *)options[section]).count + 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString * CellIdentifier = @"OptionCell";
	OrderedOption * option = options[indexPath.section][indexPath.row - 1];
	
	OptionCell * cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
	
	if (!cell) {
		cell = [[NSBundle mainBundle] loadNibNamed:@"OptionCell" owner:self options:nil][0];
		cell.textField.delegate = self;
		//    cell = [[DishCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
	}
	
	[cell setOption:option.option andTag:kOptionCellTag(indexPath.section, indexPath.row - 1)];
	[cell setQuantity:option.qty.integerValue];
	
	return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	OptionCell * cell = (OptionCell *)[self tableView:tableView cellForRowAtIndexPath:indexPath];
	[cell.textField becomeFirstResponder];
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}

#pragma mark - UIScrollView delegate methods

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
	[responder resignFirstResponder];
}

- (void)scrollViewDidEndScrollingAnimation:(UIScrollView *)scrollView {
	[responder resignFirstResponder];
}

#pragma mark - UITextView delegate methods


- (void)textViewDidBeginEditing:(UITextView *)textView {
	gesture.enabled = YES;
	if (!allOptions.count) return ; // This means the text view is already in the middle of the page

	[UIView animateWithDuration:0.3 animations:^{
		CGRect frame = commentSuperview.frame;
		frame.origin.y -= keyboardSize.height - 40;
		commentSuperview.frame = frame;
		expandableTableView.alpha = 0;
	}];
}

- (void)textViewDidEndEditing:(UITextView *)textView {
	gesture.enabled = NO;
	if (!allOptions.count) return ; // This means the text view is already in the middle of the page

	[UIView animateWithDuration:0.3 animations:^{
		CGRect frame = commentSuperview.frame;
		frame.origin.y += keyboardSize.height - 40;
		commentSuperview.frame = frame;
		expandableTableView.alpha = 1;
	}];
}


#pragma mark - UITextField delegate methods

- (void)textFieldDidBeginEditing:(UITextField *)textField {
	textField.text = @"";
	responder = textField;
	gesture.enabled = YES;
}

- (void)textFieldDidEndEditing:(UITextField *)textField {
	if (!textField.text.length)
		textField.text = @"0";
	gesture.enabled = NO;

	NSUInteger row = kOptionCellRowForTag(textField.tag);
	NSUInteger section = kOptionCellSectionForTag(textField.tag);
	
	OrderedOption * ordered = options[section][row];
	ordered.qty = @(textField.text.integerValue);
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
	NSString * text = [textField.text stringByReplacingCharactersInRange:range withString:string];
	if (!text.length)
		text = @"0";
	
	NSUInteger row = kOptionCellRowForTag(textField.tag);
	NSUInteger section = kOptionCellSectionForTag(textField.tag);
	
	OrderedOption * ordered = options[section][row];
	ordered.qty = @(text.integerValue);
	
	return YES;
}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
