//
//  CommandViewController.m
//  FastOrdering
//
//  Created by Sunder on 04/03/2015.
//  Copyright (c) 2015 lucas.bergognon. All rights reserved.
//

#import "CommandViewController.h"
#import "CommandMenuViewController.h"
#import "Order+Custom.h"
#import "NSManagedObject+create.h"
#import "SocketHelper.h"
#import "AppDelegate.h"
#import "UNIRest.h"
#import "EditDishViewController.h"

// Uncomment the following line to not test if the fields are blank
//#define kDoNotTestFields

@interface CommandViewController ()

@end

@implementation CommandViewController

- (void)viewDidLoad {
	[super viewDidLoad];
	[SocketHelper.sharedHelper pushDelegate:self];

	
	AppDelegate * appDelegate = (AppDelegate *)UIApplication.sharedApplication.delegate;
	[appDelegate createNestedContext];
	context = appDelegate.managedObjectContext;
	
	if (self.order) {
		self.order = [context objectWithID:self.order.objectID];
		forceReview = YES;
	} else {
		self.order = [Order create];
		forceReview = NO;
	}

	menuModel = [OrderMenuModel new];
	carteModel = [[OrderALaCarteModel alloc] initWithOrder:self.order];
	reviewModel = [OrderReviewModel new];
	menuModel.delegate = self;
	reviewModel.delegate = self;
	reviewModel.order = self.order;
	reviewModel.tableView = reviewTableView;
	menuTableView.dataSource = menuModel;
	menuTableView.delegate = menuModel;
	carteTableView.dataSource = carteModel;
	carteTableView.delegate = carteModel;
	reviewTableView.delegate = reviewModel;
	reviewTableView.dataSource = reviewModel;
	
	numPAField.text = self.order.dinerNumber.integerValue ? self.order.dinerNumber.stringValue : nil;
	tableNumberField.text = self.order.table_id ? self.order.table_id : nil;
	commentTextView.text = self.order.comments;
	
	[menuButton setTitle:NSLocalizedString(@"Menus", @"").uppercaseString forState:UIControlStateNormal];
	[alacarteButton setTitle:NSLocalizedString(@"A la carte", @"").uppercaseString forState:UIControlStateNormal];
	[reviewButton setTitle:NSLocalizedString(@"Order", @"").uppercaseString forState:UIControlStateNormal];
	[addAlacarteButton setTitle:NSLocalizedString(@"add", @"").uppercaseString forState:UIControlStateNormal];
	[orderButton setTitle:NSLocalizedString(@"order", @"").uppercaseString forState:UIControlStateNormal];

	tableNumberLabel.text = NSLocalizedString(tableNumberLabel.text, @"");
	numPALabel.text = NSLocalizedString(numPALabel.text, @"");
	commentLabel.text = NSLocalizedString(commentLabel.text, @"");
	
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(keyboardWillShow:)
												 name:UIKeyboardWillShowNotification
											   object:nil];
 
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(keyboardWillHide:)
												 name:UIKeyboardWillHideNotification
											   object:nil];

	[self buttonClicked:(forceReview ? reviewButton : menuButton)];
}

- (void)viewWillDisappear:(BOOL)animated {
	AppDelegate * delegate = (AppDelegate *)UIApplication.sharedApplication.delegate;
	[super viewWillDisappear:animated];
	
	[SocketHelper.sharedHelper popDelegate:self];
	[presentController.view removeFromSuperview];
	presentController = nil;

	if (didOrder) {
		[delegate mergeNestedContext];
		[delegate saveContext];
	} else if (!showingController) {
		[delegate deleteNestedContext];
	}
}

- (void)didReceiveMemoryWarning {
	[super didReceiveMemoryWarning];
	// Dispose of any resources that can be recreated.
}

#pragma mark - Helper methods

- (void)orderFailed {
	if (loaderView.hidden) return; // Error didn't happen when ordering

	loaderView.hidden = YES;
	[[[UIAlertView alloc] initWithTitle:NSLocalizedString(@"error", @"") message:NSLocalizedString(@"Order_ErrorMessage", @"") delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil] show];
}

- (BOOL)testFields {
	// Bypass the security for test purposes
#ifdef kDoNotTestFields
	return YES;
#endif
	
	if (!numPAField.text.length || !tableNumberField.text.length) {
		[[[UIAlertView alloc] initWithTitle:NSLocalizedString(@"error", @"") message:NSLocalizedString(@"Order_FieldErrorMessage", @"") delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil] show];
		return NO;
	}
	
	return YES;
}

- (void)showController:(UIViewController *)controller {
	showingController = YES;
	[self.mainController.navigationController pushViewController:controller animated:YES];
//	if (presentController) {
//		[presentController.view removeFromSuperview];
//		presentController = nil;
//	}
//	
//	presentController = controller;
//
//	CGRect frame = presentController.view.frame;
//	frame.origin.x += frame.size.width;
//	presentController.view.frame = frame;
//	
//	[self.view addSubview:presentController.view];
//	[UIView animateWithDuration:0.5 animations:^{
//		CGRect frame = presentController.view.frame;
//		frame.origin.x -= frame.size.width;
//		presentController.view.frame = frame;
//	}];
}

- (void)popPresentController {
	showingController = NO;
	[self.mainController.navigationController popViewControllerAnimated:YES];
//	[UIView animateWithDuration:0.5 animations:^{
//		CGRect frame = presentController.view.frame;
//		frame.origin.x += frame.size.width;
//		presentController.view.frame = frame;
//	} completion:^(BOOL finished) {
//		[presentController.view removeFromSuperview];
//		presentController = nil;
//	}];
}

#pragma mark - UITextView and UITextField delegate methods

- (void)textFieldDidBeginEditing:(UITextField *)textField {
	gesture.enabled = YES;
}

- (void)textFieldDidEndEditing:(UITextField *)textField {
	gesture.enabled = NO;
}

- (void)textViewDidBeginEditing:(UITextView *)textView {
	[UIView animateWithDuration:0.3 animations:^{
		CGRect frame = commentView.frame;
		frame.origin.y -= keyboardSize.height - 40;
		commentView.frame = frame;
		reviewTableView.alpha = 0;
		gesture.enabled = YES;
	}];
}

- (void)textViewDidEndEditing:(UITextView *)textView {
	[UIView animateWithDuration:0.3 animations:^{
		CGRect frame = commentView.frame;
		frame.origin.y += keyboardSize.height - 40;
		commentView.frame = frame;
		reviewTableView.alpha = 1;
		gesture.enabled = NO;
	}];
}

#pragma mark - Keyboard methods

- (void)keyboardWillShow:(NSNotification *)notification {
	keyboardSize = [[[notification userInfo] objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
}

- (void)keyboardWillHide:(NSNotification *)notification {
	keyboardSize = [[[notification userInfo] objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
}

#pragma mark - Menu and Review delegate methods

- (void)menuCompositionClicked:(MenuComposition *)composition {
	CommandMenuViewController * controller = [[CommandMenuViewController alloc] initWithNibName:@"CommandMenuView" bundle:nil];
	
	controller.composition = composition;
	controller.delegate = self;
	[self showController:controller];
}

- (void)orderedDishClicked:(OrderedDish *)dish {
	EditDishViewController * controller = [[EditDishViewController alloc] initWithNibName:@"EditDishView" bundle:nil];

	controller.dish = dish;
	controller.delegate = self;
	[self showController:controller];
}

#pragma mark - CommandMenuView delegate methods

- (void)didOrderDishes:(NSArray *)dishes {
	[self.order addDishes:[NSSet setWithArray:dishes]];

	[self buttonClicked:reviewButton];
}

- (void)popCommandMenuView {
	[self popPresentController];
}

#pragma mark - EditDishView delegate methods

- (void)popEditDishView {
	[self popPresentController];
}

#pragma mark - IBAction methods

- (IBAction)endEditing {
	[self.view endEditing:YES];
}

- (IBAction)buttonClicked:(UIButton *)sender {
	for (UIView * v in clickedViews)
		v.hidden = v.tag != sender.tag;
	for (UIView * v in contentViews)
		v.hidden = v.tag != sender.tag;

	if (sender.tag == alacarteView.tag) {
		[carteModel reloadData];
		[carteTableView reloadData];
		carteModel.editing = YES;
	} else {
		[self.order sanitize];
		carteModel.editing = NO;
	}
	[reviewModel reloadData];
}

- (IBAction)takeOrder {
	if (!self.testFields) return;

	SocketHelper * helper = [SocketHelper sharedHelper];
	[self.order sanitize];
	self.order.table_id = tableNumberField.text;
	self.order.dinerNumber = @(numPAField.text.integerValue);
	self.order.comments = commentTextView.text;
	
	if (!helper.socket.isConnected) return [self orderFailed];

	DPPLog(@"Will send JSON :\n%@\n\n", self.order.toJSONString);
	self.mainController.doNotSync = YES;

	loaderView.hidden = NO;
	[helper.socket sendEvent:@"send_order" withData:self.order.toJSON andAcknowledge:^(id argsData) {
		NSDictionary * dict = argsData;

		if (dict[@"error"] || !dict.count) {
			PPLog(@"%@", dict[@"error"]);
			self.mainController.doNotSync = NO;
			[timer fire];
		} else {
			[timer invalidate];
			
			self.order.createdAt = [NSDate date];
			self.order.updatedAt = [NSDate date];
			self.order.serverId = dict[@"numOrder"];
			dispatch_async(dispatch_get_main_queue(), ^{
				loaderView.hidden = YES;
				didOrder = YES;
				[self.mainController goBackToMainPage];
				self.mainController.doNotSync = NO;
			});
		}
	}];

	// find a way to get a callback
	timer = [NSTimer scheduledTimerWithTimeInterval:5.0 target:self selector:@selector(orderFailed) userInfo:nil repeats:NO];
}

#pragma mark - SocketIO delegate methods

- (void)socketIO:(SocketIO *)socket didReceiveMessage:(SocketIOPacket *)packet {
	PPLog(@"MESSAGE %@", packet);
}

- (void)socketIO:(SocketIO *)socket didReceiveJSON:(SocketIOPacket *)packet {
	PPLog(@"JSON %@", packet);
}

- (void)socketIO:(SocketIO *)socket didReceiveEvent:(SocketIOPacket *)packet {
	PPLog(@"EVENT %@", packet);
}

- (void)socketIO:(SocketIO *)socket didSendMessage:(SocketIOPacket *)packet {
	PPLog(@"SENT %@", packet);
}

- (void)socketIO:(SocketIO *)socket onError:(NSError *)error {
	[timer invalidate];
	[self orderFailed];
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
