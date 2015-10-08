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

@interface EditDishViewController ()

@end

#define kOptionCellTag(section, row)	((((section) + 1) * 100) + (row) + 1)
#define kOptionCellSectionForTag(tag)	(((tag) / 100) - 1)
#define kOptionCellRowForTag(tag)		(((tag) % 100) - 1)

@implementation EditDishViewController

- (void)viewDidLoad {
    [super viewDidLoad];
	
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(keyboardWillShow:)
												 name:UIKeyboardWillShowNotification
											   object:nil];
 
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(keyboardWillHide:)
												 name:UIKeyboardWillHideNotification
											   object:nil];

	commentLabel.text = NSLocalizedString(commentLabel.text, @"");
	[validateButton setTitle:NSLocalizedString(@"validate", @"").uppercaseString forState:UIControlStateNormal];
	[backButton setTitle:NSLocalizedString(@"back", @"").capitalizedString forState:UIControlStateNormal];
}

- (void)viewWillDisappear:(BOOL)animated {
	[super viewWillDisappear:animated];
	[[NSNotificationCenter defaultCenter] removeObserver:self.view];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Keyboard methods

- (void)keyboardWillShow:(NSNotification *)notification {
	CGSize keyboardSize = [[[notification userInfo] objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
	CGRect frame = commentSuperview.frame;
	frame.origin.y -= keyboardSize.height;
	commentSuperview.frame = frame;
	expandableTableView.alpha = 0;
	gesture.enabled = YES;
}

- (void)keyboardWillHide:(NSNotification *)notification {
	CGSize keyboardSize = [[[notification userInfo] objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
	CGRect frame = commentSuperview.frame;
	frame.origin.y += keyboardSize.height;
	commentSuperview.frame = frame;
	expandableTableView.alpha = 1;
	gesture.enabled = NO;
}

#pragma mark - IBAction methods

- (IBAction)endEditing {
	[self.view endEditing:YES];
}

- (IBAction)validate {
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
		//        cell = [[NSBundle mainBundle] loadNibNamed:@"NotificationCell" owner:self options:nil][0];
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

#pragma mark - UITextField delegate methods

- (void)textFieldDidBeginEditing:(UITextField *)textField {
	textField.text = @"";
	responder = textField;
}

- (void)textFieldDidEndEditing:(UITextField *)textField {
	if (!textField.text.length)
		textField.text = @"0";
	
	NSUInteger row = kOptionCellRowForTag(textField.tag);
	NSUInteger section = kOptionCellSectionForTag(textField.tag);
	
	OrderedDish * dish = options[section][row];
	dish.quantity = @(textField.text.integerValue);
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
	NSString * text = [textField.text stringByReplacingCharactersInRange:range withString:string];
	if (!text.length)
		text = @"0";
	
	NSUInteger row = kOptionCellRowForTag(textField.tag);
	NSUInteger section = kOptionCellSectionForTag(textField.tag);
	
	OrderedDish * dish = options[section][row];
	dish.quantity = @(text.integerValue);
	
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
