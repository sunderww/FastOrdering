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
	
	if (self.order) {
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
	tableNumberField.text = self.order.numTable ? self.order.numTable : nil;
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
	[super viewWillDisappear:animated];

	AppDelegate * delegate = ((AppDelegate *)UIApplication.sharedApplication.delegate);
	if (!didOrder && !forceReview)
		[delegate.managedObjectContext deleteObject:self.order];

	[delegate saveContext];
}

- (void)didReceiveMemoryWarning {
	[super didReceiveMemoryWarning];
	// Dispose of any resources that can be recreated.
}

#pragma mark - Helper methods

- (void)orderFailed {
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
	if (presentController) {
		[presentController.view removeFromSuperview];
		presentController = nil;
	}
	
	presentController = controller;

	CGRect frame = presentController.view.frame;
	frame.origin.x += frame.size.width;
	presentController.view.frame = frame;
	
	[self.view addSubview:presentController.view];
	[UIView animateWithDuration:0.5 animations:^{
		CGRect frame = presentController.view.frame;
		frame.origin.x -= frame.size.width;
		presentController.view.frame = frame;
	}];
}

- (void)popPresentController {
	[UIView animateWithDuration:0.5 animations:^{
		CGRect frame = presentController.view.frame;
		frame.origin.x += frame.size.width;
		presentController.view.frame = frame;
	} completion:^(BOOL finished) {
		[presentController.view removeFromSuperview];
		presentController = nil;
	}];
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

- (void)didCreateOrderContent:(OrderContent *)content {
	AppDelegate * delegate = ((AppDelegate *)UIApplication.sharedApplication.delegate);

	// Do not add an empty order content
	// Now that [Order sanitize] exists, maybe it could be used
	if (content.dishes.count > 0) {
		[self.order addOrderContentsObject:content];
	} else {
		[delegate.managedObjectContext deleteObject:content];
	}
	
	// Save the db
	[delegate saveContext];
	
	// go to review page
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
	self.order.numTable = tableNumberField.text;
	self.order.dinerNumber = @(numPAField.text.integerValue);
	self.order.comments = commentTextView.text;

//	DPPLog(@"FORCE FAIL");
//	return [self orderFailed];
	
	if (!helper.socket.isConnected) return [self orderFailed];
	
	DPPLog(@"Will send JSON :\n%@\n\n", self.order.toJSONString);

	loaderView.hidden = NO;
	[helper pushDelegate:self];
	[helper.socket sendEvent:@"send_order" withData:self.order.toJSON andAcknowledge:^(id argsData) {
		NSDictionary * dict = argsData;
		
		if (dict[@"error"] || !dict.count) {
			PPLog(@"%@", dict[@"error"]);
			[timer fire];
		} else {
			[timer invalidate];
			loaderView.hidden = YES;
			didOrder = YES;
			
			self.order.createdAt = [NSDate date];
			self.order.updatedAt = [NSDate date];
			self.order.serverId = dict[@"numOrder"];
			[self.mainController goBackToMainPage];
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
